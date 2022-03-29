package com.cognixia.jump.util;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.cognixia.jump.model.Bet;
import com.cognixia.jump.model.City;

@Service
public class WeatherUtil {
	 
	private static String webtoken = "20cb7c3f70bc171cda7bac3503a38ff1";

	public static String getWebtoken() {
		return webtoken;
	}

	public static void setWebtoken(String webtokentoSet) {
		WeatherUtil.webtoken = webtokentoSet;
	}
	
	/**
	 * This aspirational method should be able to create queues of bets to be processed by finding bets on the proper dates. 
	 * If the aspirational goal is met then it should be able to run in the background as a detached process
	 */
	public void betScheduler(){
	
	}
	
	
	
	
	/**
	 * This method will handle the queue of bets, testing each one until the queue is taken care of.
	 * It will handle IOExceptions either through retesting the bets or flagging the bets for admin attention
	 * 
	 */
	public void betQueueManager() {
		
	}
	
	
	
	/**
	 * This method will aim to test and confirm if a bets conditions are met. It will only check date and time on a basic level.
	 * It will initally only test temperature however this functionality along with other fields might be upaletered
	 * The return object is a newly created bet that is used to update the previous bet in the database 
	 * @param Bet
	 * @return Bet
	 */
	public Bet testBet(Bet bet) throws IOException{
		
		
			callWeatherApi(bet.getCity());
		
		
		
		
		return bet;		
	}
	
	
	/**
	*The callWeatherApi method intakes the City object from a bet and uses that to extract the city code
	*it uses this to call the openWeather API
	*if it gets a favorable response it will copy the response down the response JSON into a string.
	*It will throw a IOException if there is a problem with pining the API
	
	*/
	public String callWeatherApi(City city ) throws IOException {
		StringBuilder urlToUse = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?id=");
		urlToUse.append(city.getCode());     //This segment prepares the URL to ping
		urlToUse.append("&appid=");
		urlToUse.append(getWebtoken());
		HttpURLConnection con;
		StringBuilder weatherResponse = new StringBuilder();
		try {
			URL url = new URL(urlToUse.toString());
			con = (HttpURLConnection) url.openConnection(); //This segment opens the connection and prepares the connection
			con.setRequestMethod("GET");
			con.setConnectTimeout(4000);
			con.setReadTimeout(4000);
			int status = con.getResponseCode();			
			if(status == 200) {
				BufferedReader response = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String input;
				while((input = response.readLine()) != null) {
					weatherResponse.append(input); //This segment reads from the api and adds it to a string builder
				}
				response.close();
				con.disconnect();
			}else {
				throw new IOException("Connection to API could not be established");
			}
		} catch (MalformedURLException e) {
			
			e.printStackTrace(); //FINISH ME
		} 
		
		return weatherResponse.toString();
		
		
	}

}
