package com.cognixia.jump.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping("/admin/bets")
	public List<Bet> getAllBets(){
		return betserv.findAllBet();
	}
	
	@GetMapping("/bets/{id}")
	public ResponseEntity<Bet> findBetById(@Valid @PathVariable int id)throws ResourceNotFoundException{

		Bet bet = betserv.findBetById(id);
		if(bet != null) {
			 return ResponseEntity.status(201).body(bet);
		}
		
		throw new ResourceNotFoundException("No bet with that Id found");
	
	}
	
	
	@PostMapping("/bets")
	public ResponseEntity<Bet> createBet(@Valid @RequestBody Bet bet, Principal principal) throws ResourceNotFoundException{
		
		User user = userserv.findUserByUsername(principal.getName());
		LocalDateTime localdatetime = LocalDateTime.now();
		
		if(bet.getWager() <= user.getCredit()) {
			bet.setBet_id(null);
			bet.setCreation_date(localdatetime);
			bet.setUser(user);
			
			user.setCredit(user.getCredit()-bet.getWager());
			
			Bet created = betserv.createBet(bet);
			
			return ResponseEntity.status(201).body(created);
		}
		
		throw new ResourceNotFoundException("user dont have enough wager");
	}
	
	
	@PutMapping("/bets")
	public ResponseEntity<?> editBet(@Valid @RequestBody Bet bet) throws ResourceNotFoundException{
		
		if( betserv.findBetById(bet.getBet_id()) != null) {
			Bet updated =betserv.updateBet(bet);
			return ResponseEntity.status(200).body(updated);
		}
		
		throw new ResourceNotFoundException("user with id: "+ bet.getBet_id()+ " was not found");		
	}
	
	@DeleteMapping("/bets/{id}")
	public ResponseEntity<?> deleteBet(@PathVariable int id){
		if(betserv.deleteBet(id)) {
			return ResponseEntity.status(200).body("bet with id: "+ id + " was deleted");
		}
		return ResponseEntity.status(400).body("id not found");
	}
	
	
	
	

}
