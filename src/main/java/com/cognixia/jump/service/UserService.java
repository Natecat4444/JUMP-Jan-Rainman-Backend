package com.cognixia.jump.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository repo;
	
	public List<User> getAllUsers(){
		return repo.findAll();
	}
	
	public User createUser(User user) {
		return repo.save(user);
	}
	
	public User findUserByUsername(String username) {
		Optional<User> found = repo.findByUsername(username);
		return found.get();
	}
	
	public boolean deleteUser(int id) {
		if (repo.existsById(id)) {
			repo.deleteById(id);
			return true;
		}
		return false;
	}
	
	public User updateUser(User user) {
		return repo.save(user);
	}
	
	public List<User> getLeaderboard(){
		return repo.getLeaderboard();
	}

}
