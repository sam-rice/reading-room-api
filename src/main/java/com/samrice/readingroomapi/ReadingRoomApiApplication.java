package com.samrice.readingroomapi;

import com.samrice.readingroomapi.filters.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReadingRoomApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadingRoomApiApplication.class, args);
	}
}
