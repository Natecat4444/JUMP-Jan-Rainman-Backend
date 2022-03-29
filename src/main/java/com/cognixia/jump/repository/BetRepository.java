package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Bet;

@Repository
public interface BetRepository extends JpaRepository<Bet, Integer> {

	@Query("select b from Bet b where user_id = ?1")
	public List<Bet> findAllByUserId(Integer user_id);
	
}
