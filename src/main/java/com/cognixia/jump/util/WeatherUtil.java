package com.cognixia.jump.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cognixia.jump.model.Bet;
import com.cognixia.jump.model.Bet.Status;
import com.cognixia.jump.service.BetService;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.model.City;
import com.cognixia.jump.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class WeatherUtil {

	@Autowired
	UserService userserv;
	@Autowired
	BetService betserv;

	public void payoutBet(Bet bet) {
		User updated = bet.getUser();
		updated.setCredit(updated.getCredit()+(bet.getWager()*2) );
		userserv.updateUser(updated);
		betserv.updateBet(bet);
		
	}
	

	private static String webtoken = "20cb7c3f70bc171cda7bac3503a38ff1";

	public static String getWebtoken() {
		return webtoken;
	}

	public static void setWebtoken(String webtokentoSet) {
		WeatherUtil.webtoken = webtokentoSet;
	}

	public static Queue<Bet> betQueue = new PriorityQueue<Bet>();

	/**
	 * This aspirational method should be able to create queues of bets to be
	 * processed by finding bets on the proper dates. If the aspirational goal is
	 * met then it should be able to run in the background as a detached process
	 */
	@Scheduled(cron ="0 0 12 1/1 * ? *")
	public void betScheduler() { //THIS IS A BAD IMPLEMENTATION PLEASE FIX ME
		List<Bet> betList = betserv.findAllBet();

		for(Bet x : betList) {
		 LocalDate betDate = x.getForecast_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		 if(betDate.equals(LocalDate.now())) {
			 betQueue.add(x);
		 }
		}

		betQueueManager();
	}

	/**
	 * This method will handle the queue of bets, testing each one until the queue
	 * is taken care of. It will handle IOExceptions either through retesting the
	 * bets or flagging the bets for admin attention
	 * 
	 */
	public boolean betQueueManager() {
		boolean breakoutflag = false;
		int betfailures = 0;
		Bet betToTest;
		while ((!(betQueue.isEmpty())) && (!(breakoutflag))) {
			betToTest = betQueue.poll();
			
			try {
				betToTest = testBet(betToTest);
				if(betToTest.getStatus() ==Status.WINNER) {
					payoutBet(betToTest);
				}
			} catch (IOException e) {
				if(betfailures > 1 ) { //THIS SHOULD BE A CHANGEABLE THRESHOLD
					//HERE WOULD BE ADMIN FLAGGING CODE
				}else {
					betQueue.add(betToTest); //readd bet to test queue to see if the error can be overcome
					betfailures++;
				}
				e.printStackTrace();
			}
		}

		return true;// This would return something of real value else if there was a way to handle bet admin flagg

	}

	/**
	 * This method will aim to test and confirm if a bets conditions are met. It
	 * will only check date and time on a basic level. It will initally only test
	 * temperature however this functionality along with other fields might be
	 * upaletered The return object is a newly created bet that is used to update
	 * the previous bet in the database
	 * 
	 * @param Bet
	 * @return Bet
	 */
	public Bet testBet(Bet bet) throws IOException {

		int hardcodedMOE = 5; // THIS IS A HARDCODED MARGIN OF ERROR It can be editted to be adjustable

		String response = callWeatherApi(bet.getCity());
		JsonElement jsonElement = JsonParser.parseString(response.toString()); // This is all prework to parse all
																				// elements of the weather report as it
																				// is at the moment.
		JsonObject weatherObject = jsonElement.getAsJsonObject(); // This will allow rapid expansion beyond what was
																	// already
		JsonObject coordReport = weatherObject.getAsJsonObject("coord");
		JsonObject mainReport = weatherObject.getAsJsonObject("main");
		JsonObject descriptiveReport = weatherObject.getAsJsonObject("weather");
		JsonObject windReport = weatherObject.getAsJsonObject("wind");

		double tempKelvin = mainReport.get("temp").getAsDouble();
		int correcttemp = convertKelvintoF(tempKelvin);

		if (betTempcheck(bet.getTemperature(), correcttemp, hardcodedMOE)) {
			bet.setStatus(Status.WINNER);// FIGURE OUT WAY TO GET REWARDS TO USER IN A WAY THAT IS AUTOMATICALLY SAVED
											// SOMEHOW
		} else {
			bet.setStatus(Status.LOST);
		}
		return bet;
	}

	/**
	 * This method will return true if the bet wins and false if it does not
	 * 
	 * @param betTemp
	 * @param realTemp
	 * @param MOE
	 * @return
	 */

	public boolean betTempcheck(int betTemp, int realTemp, int MOE) {
		if ((betTemp >= realTemp) && (betTemp <= (realTemp + MOE))) {
			return true;
		} else if ((betTemp <= realTemp) && (betTemp >= (realTemp - MOE))) {
			return true;
		} else {
			return false;
		}

	}

	public int convertKelvintoF(double kelvin) {
		Double faren1 = (1.8 * (kelvin - 273)) + 32;
		int faren2 = faren1.intValue();
		return faren2;
	}

	/**
	 * The callWeatherApi method intakes the City object from a bet and uses that to
	 * extract the city code it uses this to call the openWeather API if it gets a
	 * favorable response it will copy the response down the response JSON into a
	 * string. It will throw a IOException if there is a problem with pining the API
	 * 
	 */
	public String callWeatherApi(City city) throws IOException {
		StringBuilder urlToUse = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?id=");
		urlToUse.append(city.getCode()); // This segment prepares the URL to ping
		urlToUse.append("&appid=");
		urlToUse.append(getWebtoken());
		HttpURLConnection con;
		StringBuilder weatherResponse = new StringBuilder();
		try {
			URL url = new URL(urlToUse.toString());
			con = (HttpURLConnection) url.openConnection(); // This segment opens the connection and prepares the
															// connection
			con.setRequestMethod("GET");
			con.setConnectTimeout(4000);
			con.setReadTimeout(4000);
			int status = con.getResponseCode();
			if (status == 200) {
				BufferedReader response = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String input;
				while ((input = response.readLine()) != null) {
					weatherResponse.append(input); // This segment reads from the api and adds it to a string builder
				}
				response.close();
				con.disconnect();
			} else {
				throw new IOException("Connection to API could not be established");
			}
		} catch (MalformedURLException e) {

			e.printStackTrace(); // FINISH ME
		}

		return weatherResponse.toString();

	}

}
