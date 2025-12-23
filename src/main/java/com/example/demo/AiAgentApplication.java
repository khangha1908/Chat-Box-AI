package com.example.demo;

import com.example.demo.service.WebSearchService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@SpringBootApplication
public class AiAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiAgentApplication.class, args);
	}

	@Bean("internetSearch")
	@Description("Tìm kiếm thông tin trên internet.")
	public Function<WebSearchService.Request, WebSearchService.Response> internetSearch() {
		return new WebSearchService();
	}
}