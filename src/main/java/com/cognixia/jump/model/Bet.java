package com.cognixia.jump.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class Bet {
	
	private static enum Status{
		WINNER, LOST, PENDING	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "ID for Bet",
	example = "1", 
	required = true)
	@Column(name="bet_id")
	private Integer bet_id;
	
	@Schema(description = "bet on temperature in Fahrenheit",
			example = "34", 
			required = true)
	@NotNull(message = "temperature can't be null")
	@Min(value = -80)
	@Max(value = 135)
	private Integer temperature;
	
	@Schema(description = "forecasted date ",
			example = "01-13-2023", 
			required = true)
	@NotNull(message = "date can't be null")
	private Date forecast_date;
	
	@Schema(description = "staus of the bet",
			example = "WINNER or LOST", 
			required = true)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Schema(description = "wager for the bet",
			example = "1", 
			required = true)
	@NotNull(message = "wager can't be null")
	private Integer wager;
	
	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime creation_date;
	
	@ManyToOne
	@Schema(description = "connection to user table by user id",
			required = true)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@Schema(description ="connection to city table by city id",
			required = true)
	@JoinColumn(name = "city_id")
	private City city;
	
	public Bet() {
		this(null, null, null, Status.LOST, 1, null, null, null);
	}

	public Bet(Integer bet_id,
			@NotNull(message = "temperature can't be null") @Size(min = -80, max = 135) Integer temperature,
			@NotNull(message = "date can't be null") Date forecast_date, Status status,
			@NotNull(message = "wager can't be null") @Size(min = 1) Integer wager, LocalDateTime creation_date,
			User user, City city) {
		super();
		this.bet_id = bet_id;
		this.temperature = temperature;
		this.forecast_date = forecast_date;
		this.status = status;
		this.wager = wager;
		this.creation_date = creation_date;
		this.user = user;
		this.city = city;
	}

	public Integer getBet_id() {
		return bet_id;
	}

	public void setBet_id(Integer bet_id) {
		this.bet_id = bet_id;
	}

	public Integer getTemperature() {
		return temperature;
	}

	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	public Date getForecast_date() {
		return forecast_date;
	}

	public void setForecast_date(Date forecast_date) {
		this.forecast_date = forecast_date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getWager() {
		return wager;
	}

	public void setWager(Integer wager) {
		this.wager = wager;
	}

	public LocalDateTime getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(LocalDateTime creation_date) {
		this.creation_date = creation_date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Bet [bet_id=" + bet_id + ", temperature=" + temperature + ", forecast_date=" + forecast_date
				+ ", status=" + status + ", wager=" + wager + ", creation_date=" + creation_date + ", user=" + user
				+ ", city=" + city + "]";
	}
	
	
	
}
