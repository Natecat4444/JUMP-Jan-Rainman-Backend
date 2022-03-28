package com.cognixia.jump.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.City;
import com.cognixia.jump.repository.CityRepository;

@RequestMapping("/api")
@RestController
public class CityController {
	@Autowired
	CityRepository repo;
	
	@GetMapping("/cities")
	public List<City> getAllCities(){
		return repo.findAll();
	}
}
