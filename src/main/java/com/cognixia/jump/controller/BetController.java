package com.cognixia.jump.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.model.Bet;
import com.cognixia.jump.model.User;
import com.cognixia.jump.service.BetService;
import com.cognixia.jump.service.UserService;

@RequestMapping("/api")
@RestController
public class BetController {
	
	@Autowired
	BetService betserv;
	
	@Autowired
	UserService userserv;
	
	/**
	 * method to get list of all bets of user
	 * using user_id
	 * @param principal to get user name
	 * @return List of bets 
	 */
	@GetMapping("/bets")
	public List<Bet> getAllUserBets(Principal principal){
		User user = userserv.findUserByUsername(principal.getName());
		return betserv.findBetByUserId(user.getUserID());
	}
	
	/**
	 * get all pending bets of user
	 * @param principal
	 * @return List of pending bets
	 */
	@GetMapping("/bets/pending")
	public List<Bet> getAllPendingBets(Principal principal){
		User user = userserv.findUserByUsername(principal.getName());
		return betserv.findPendingBets(user.getUserID());
	}
	
	/**
	 * get all completed bets of user
	 * @param principal
	 * @return List of completed bets
	 */
	@GetMapping("/bets/completed")
	public List<Bet> getAllCompletedBets(Principal principal){
		User user = userserv.findUserByUsername(principal.getName());
		return betserv.findCompletedBets(user.getUserID());
	}
	
	/**
	 * creating a bet if the wager is less than credit available
	 * @param bet
	 * @param principal
	 * @return ResponseEntity with status, Bet
	 * @throws ResourceNotFoundException
	 */
	@PostMapping("/bets")
	public ResponseEntity<Bet> createBet(@Valid @RequestBody Bet bet, Principal principal) throws ResourceNotFoundException{
		
		User user = userserv.findUserByUsername(principal.getName());
		LocalDateTime localdatetime = LocalDateTime.now();
		
		if(bet.getWager() <= user.getCredit()) {
			bet.setBet_id(null);
			bet.setCreation_date(localdatetime);
			bet.setUser(user);
			
			Bet created = betserv.createBet(bet);
			
			return ResponseEntity.status(201).body(created);
		}
		
		throw new ResourceNotFoundException("user dont have enough wager");
	}
	
	

}
