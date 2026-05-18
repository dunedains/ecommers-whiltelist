package com.ecommers.whitelist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WhitelistApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhitelistApplication.class, args);
	}

}
