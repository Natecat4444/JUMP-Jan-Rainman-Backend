package com.cognixia.jump.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.cognixia.jump.controller.UserController;
import com.cognixia.jump.model.City;
import com.cognixia.jump.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class WeatherUtilTest {
private final String STARTING_URI = "http://localhost:8080/api";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userserv;
	
	@MockBean
	UserDetailsService userDetailService;
	
	@InjectMocks
	private UserController usercontroller;
	
	@Autowired
	private ObjectMapper mapper;
	
	@InjectMocks
	private WeatherUtil weatherutil;
	
	@MockBean
	JwtUtil jwtUtil;
	@Test
	void shouldReturnApiCall() throws Exception{
		
		City city = new City(1, "Denver", 5419384, "CO");
		String x = weatherutil.callWeatherApi(city);
		assertThat(!(x.isEmpty()));
		System.out.println(x);
	}
	@Test
	void shouldTestBet() throws Exception{
		
		City city = new City(1, "Denver", 5419384, "CO");
		String x = weatherutil.callWeatherApi(city);
		assertThat(!(x.isEmpty()));
		System.out.println(x);
	}
}
