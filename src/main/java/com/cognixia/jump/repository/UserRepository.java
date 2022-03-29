package com.cognixia.jump.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	public Optional<User> findByUsername(String username);
	
	@Query(nativeQuery=true, value="select * from user order by credit DESC LIMIT 10")
	public List<User> getLeaderboard();
}
