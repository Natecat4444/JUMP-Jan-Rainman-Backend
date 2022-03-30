package com.cognixia.jump;

import java.util.TimeZone;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class JumpJanRainmanCapstoneApplication {

	@PostConstruct
	  public void init(){
	    // Setting Spring Boot SetTimeZone
	    TimeZone.setDefault(TimeZone.getTimeZone("IST"));
	  }
	
	public static void main(String[] args) {
		SpringApplication.run(JumpJanRainmanCapstoneApplication.class, args);
	}
	
	

}
