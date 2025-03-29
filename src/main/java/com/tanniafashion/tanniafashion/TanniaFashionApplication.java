package com.tanniafashion.tanniafashion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
	
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@OpenAPIDefinition
@EnableJpaAuditing
public class TanniaFashionApplication {
	public static void main(String[] args) {
		SpringApplication.run(TanniaFashionApplication.class, args);
	}
}
 