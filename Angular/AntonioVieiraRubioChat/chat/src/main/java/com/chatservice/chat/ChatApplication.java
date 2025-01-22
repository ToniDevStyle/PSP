package com.chatservice.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication(scanBasePackages = {"com.chat.config",
		"com.chat.controller",
		"com.chat.models",
		"com.chat.service",
})
@EnableMongoRepositories(basePackages = "com.chat.repositories")
@ComponentScan(basePackages = "com.chat")
public class ChatApplication {


	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}


}
