package com.cognixia.jump;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cognixia.jump.model.Bet;
import com.cognixia.jump.service.BetService;

@SpringBootTest
class JumpJanRainmanCapstoneApplicationTests {
	
	@Autowired
	private BetService betserv;

	@Test
	void contextLoads() {
	}
	
	/**
	 * unit test for the custom query 
	 * get bets attached to the user_id only
	 * 
	 * checks if the size of the array matches the one in the database
	 */
	@Test
	void shouldFindBetsByUserId() {
		
		int size = 1;
		List<Bet> userbets = betserv.findBetByUserId(1);
		
		assertEquals(size, userbets.size());
	}

}
