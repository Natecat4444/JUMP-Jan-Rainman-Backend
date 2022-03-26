package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Bet;

@Repository
public interface BetRepository extends JpaRepository<Bet, Integer> {

}
