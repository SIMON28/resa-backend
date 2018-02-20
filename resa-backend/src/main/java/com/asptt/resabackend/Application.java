package com.asptt.resabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	@Bean
//	public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
//		return new OpenEntityManagerInViewFilter();
//	}

}
