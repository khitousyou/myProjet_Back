package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
@ComponentScan(basePackages = {"com.example.demo", "com.example.demos.shared"})
public class MyApp1Application {

	public static void main(String[] args) {
		SpringApplication.run(MyApp1Application.class, args);
	}

}
