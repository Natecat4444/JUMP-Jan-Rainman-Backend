package com.cognixia.jump.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class City {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "unique ID for City",
	example = "1", 
	required = true)
	@Column(name="city_id")
	private Integer city_id;
	
	@Schema(description = "city name",
			example = "NYC", 
			required = true)
	private String name;
	
	@Schema(description = "City code",
			example = "101", 
			required = true)
	private Integer code;
	
	@Schema(description = "State of the city",
			example = "NY", 
			required = true)
	private String state;
	
	public City() {
		this(null, null, null, null);
	}

	public City(Integer city_id, String name, Integer code, String state) {
		super();
		this.city_id = city_id;
		this.name = name;
		this.code = code;
		this.state = state;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
	
}
