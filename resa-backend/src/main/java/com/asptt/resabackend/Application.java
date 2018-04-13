package com.asptt.resabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages= {"com.asptt.resabackend"})
@PropertySource({"classpath:properties/ResaPlongee.properties","classpath:properties/InscriptionPlongeePage.properties"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
