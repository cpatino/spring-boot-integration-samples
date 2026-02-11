package com.carp.sample.consumer;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerApplication {

	static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@SqsListener("my-queue")
	public void receiveMessage(String message) {
		System.out.println("Received message: " + message);
	}
}
