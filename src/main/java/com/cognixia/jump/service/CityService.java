package com.cognixia.jump.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.jump.model.City;
import com.cognixia.jump.repository.CityRepository;

@Service
public class CityService {
	
	@Autowired
	CityRepository cityrepo;
	
	public List<City> getAllCity(){
		return cityrepo.findAll();
	}
	
	public City findCityByName(String name) {
		Optional<City> found = cityrepo.findByName(name);
		return found.get();
	}
	
	public City findCityById(int id) {
		Optional<City> found = cityrepo.findById(id);
		return found.get();
	}
}
