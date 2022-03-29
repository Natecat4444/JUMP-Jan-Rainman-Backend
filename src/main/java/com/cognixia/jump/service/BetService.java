package com.cognixia.jump.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.jump.model.Bet;
import com.cognixia.jump.repository.BetRepository;

@Service
public class BetService {
	
	@Autowired
	BetRepository betrepo;
	
	public List<Bet> findAllBet(){
		return betrepo.findAll();
	}
	
	public List<Bet> findBetByUserId(int id){
		return betrepo.findAllByUserId(id);
	}
	
	public Bet findBetById(int id) {
		return betrepo.getById(id);
	}
	
	public List<Bet> findCompletedBets(int id){
		return betrepo.findAllDoneBetsByUserId(id);
	}
	
	public List<Bet> findPendingBets(int id){
		return betrepo.findAllPendingBetsByUserId(id);
	}
	
	public Bet createBet(Bet bet) {
		return betrepo.save(bet);
	}
	
	public Bet updateBet(Bet bet) {
		return betrepo.save(bet);
	}
	
	public boolean deleteBet(int id) {
		if(betrepo.existsById(id)) {
			betrepo.deleteById(id);
			return true;
		}
		return false;
	}
	
	
}
