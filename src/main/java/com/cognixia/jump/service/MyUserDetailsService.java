package com.cognixia.jump.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cognixia.jump.model.User;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserService serv;
	
	// method will be called by spring security 
	// when a request comes in , credentials (username + password) from the request will be loaded in 
	// and username will be passed to this method to load all the user details needed to do the 
	// authentication  and authorization for the request
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> userfound = Optional.of(serv.findUserByUsername( username ));
		
		// if we could not find the user, throw username not found exception
		if( userfound.isEmpty()) {
			throw new UsernameNotFoundException(username);
		}
		
		// the user details will be returned to spring security, and these details will be all the info needed to perform authentication and authorization
		
		return new MyUserDetails( userfound.get());
	}
	
}