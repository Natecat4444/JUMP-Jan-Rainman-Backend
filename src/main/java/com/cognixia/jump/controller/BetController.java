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
	
	@GetMapping("/bets")
	public List<Bet> getAllUserBets(Principal principal){
		User user = userserv.findUserByUsername(principal.getName());
		return betserv.findBetByUserId(user.getUserID());
	}
	
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
