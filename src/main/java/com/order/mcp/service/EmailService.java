package com.order.mcp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReturnInstructions(
            int orderId,
            String toEmail,
            String itemTitle,
            String refundMethod,
            String returnMethod,
            String returnType,
            String refundAmount) {

        try {
            String formattedRefundMethod = formatRefundMethod(refundMethod);
            String formattedReturnMethod = formatReturnMethod(returnMethod);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Return Instructions for Your Item: " + itemTitle);

            String body = buildEmailBody(
                    orderId,
                    itemTitle,
                    formattedRefundMethod,
                    formattedReturnMethod,
                    refundAmount,
                    returnType
            );

            helper.setText(body);
            mailSender.send(message);

            System.out.println("Email sent to: " + toEmail);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String formatRefundMethod(String refundMethod) {
        if (refundMethod == null) return "";

        String method = refundMethod.toLowerCase();

        if (method.contains("gift")) {
            return "GF-000XXX11122 (Gift Card)";
        }

        if (method.contains("original")) {
            return "XXXX-7777 (Original Payment Method)";
        }

        return refundMethod;
    }

    private String formatReturnMethod(String returnMethod) {
        if (returnMethod == null) return "";

        String method = returnMethod.toLowerCase();

        if (method.contains("store")) {
            return "Store Drop-Off";
        }

        if (method.contains("ups")) {
            return "UPS Drop-Off";
        }

        return returnMethod;
    }

    private String buildEmailBody(
            int orderId,
            String itemTitle,
            String refundMethod,
            String returnMethod,
            String refundAmount,
            String returnType) {

        if (isReturnLess(returnType)) {
            return buildReturnLessBody(orderId, itemTitle, refundMethod, refundAmount);
        }

        return buildStandardReturnBody(orderId, itemTitle, refundMethod, returnMethod, refundAmount);
    }

    private String buildReturnLessBody(
            int orderId,
            String itemTitle,
            String refundMethod,
            String refundAmount) {

        return String.format("""
            Dear Customer,

            We've received your return request regarding

            Order# %s

            Item(s): %s
            Refund method: %s
            Refund amount: %s

            You are not required to return the item. Your refund will be processed within 5–10 business days.

            Thank you for shopping with us!
            """,
                orderId,
                itemTitle,
                refundMethod,
                refundAmount);
    }

    private String buildStandardReturnBody(
            int orderId,
            String itemTitle,
            String refundMethod,
            String returnMethod,
            String refundAmount) {

        String attachmentText = returnMethod.contains("Store")
                ? "Attached is your QR code for store drop-off."
                : "Attached is your UPS label for the return.";

        return String.format("""
            Dear Customer,

            We've received your return request regarding

            Order# %s

            Item(s): %s
            Refund method: %s
            Return method: %s
            Refund amount: %s

            %s

            Thank you for shopping with us!
            """,
                orderId,
                itemTitle,
                refundMethod,
                returnMethod,
                refundAmount,
                attachmentText);
    }

    private boolean isReturnLess(String returnType) {
        if (returnType == null) return false;

        return returnType.equalsIgnoreCase("RETURN_LESS")
                || returnType.toLowerCase().contains("less");
    }

    public void sendEmail(String email, String messageBody) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Sorry for the inconvenience");

            helper.setText(messageBody);

            mailSender.send(message);
            System.out.println("Email sent to: " + email);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

