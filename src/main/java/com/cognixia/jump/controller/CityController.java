package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping("/city/id/{id}")
	public ResponseEntity<?> getCity(@PathVariable int id){
		Optional<City> found = repo.findById(id);
		if(found.isEmpty()) {
			return ResponseEntity.status(404).body("Not found");
		}
		return ResponseEntity.status(200).body(found.get());
	}
	
	@GetMapping("/city/{name}")
	public ResponseEntity<?> getCityName(@PathVariable String name){
		Optional<City> found = repo.findByName(name);
		if(found.isEmpty()) {
			return ResponseEntity.status(404).body("Not found");
		}
		return ResponseEntity.status(200).body(found.get());
	}
}
