package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
	
	public Optional<City> findByName(String name);
}
