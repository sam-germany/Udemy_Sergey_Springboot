package com.sunny.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sunny.admin.security.AppProperties1;

@SpringBootApplication
public class App02Application {

	public static void main(String[] args) {
		SpringApplication.run(App02Application.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	@Bean
	public SpringApplicationContext springApplicationContext() {
		  return new SpringApplicationContext();
	}
	
	
	@Bean(name = "AppProperties1")
	public AppProperties1 getAppProperties() {
		 return  new AppProperties1();
	}
}
