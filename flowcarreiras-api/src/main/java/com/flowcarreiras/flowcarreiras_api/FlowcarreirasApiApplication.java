package com.flowcarreiras.flowcarreiras_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlowcarreirasApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowcarreirasApiApplication.class, args);
	}

}
