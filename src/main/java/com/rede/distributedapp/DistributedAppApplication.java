package com.rede.distributedapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DistributedAppApplication {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(DistributedAppApplication.class, args)));
	}
}
