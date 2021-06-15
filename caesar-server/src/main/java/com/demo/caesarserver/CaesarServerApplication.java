package com.demo.caesarserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CaesarServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaesarServerApplication.class, args);
	}

}
