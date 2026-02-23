package com.order.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.mcp.service.OrderToolService;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public List<ToolCallback> tools(OrderToolService orderToolService) {
		return List.of(ToolCallbacks.from(orderToolService));
	}

}
