package com.cognixia.jump.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.model.AuthenticationRequest;
import com.cognixia.jump.model.AuthenticationResponse;
import com.cognixia.jump.model.User;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.util.JwtUtil;

@CrossOrigin(origins = "http://jump-java-final-test-bucket.s3-website.us-east-2.amazonaws.com/")
@RequestMapping("/api")
@RestController
public class UserController {
	
	// manages and handles which users are valid
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserService serv;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtil jwtUtil;
	
	/**
	 * a user will pass their credentials and get back a JWT
	 *  Once JWT is given to a user , can use JWT for every other request, 
	 *  no need to provide credentials anymore
	 * @param request
	 * @return jwt with status
	 * @throws Exception
	 */
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception {
		
		try {
			// make sure we have a valid user by checking their username and password
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()) );
		
		}catch(BadCredentialsException e) {
			// provide our own message on why login didnt work
			throw new Exception("Incorrect Username/Passoword");
		}
		
		// as long as no exception was thrown, user is valid
		
		// load in the user details for that user
		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		
		// generate the token
		final String jwt = jwtUtil.generateTokens(userDetails);
		
		// return the token
		return ResponseEntity.status(201).body(new AuthenticationResponse(jwt));
	}
	
	@GetMapping("/users")
	public List<User> getAll(){
		return serv.getAllUsers();
	}
	

	@GetMapping("/user")
	public ResponseEntity<?> getCurrentUser(Principal principle){
		User user =  serv.findUserByUsername(principle.getName());	
		return ResponseEntity.status(200).body(user);
	}
	
	@GetMapping("/user/leaderboard")
	public List<User> getLeaderboard(){
		return serv.getLeaderboard();
	}
	
	@GetMapping("/admin/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable Integer id){
		User user = serv.findUserByUserID(id);
		return ResponseEntity.status(200).body(user);
		
	}

	/**
	 * method to add a user 
	 * @param user
	 * @return ResponseEntiy with status of 201 and user object
	 */
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user){
		
		user.setUserID(null);
		
		user.setPassword(encoder.encode(user.getPassword()));
		
		User created = serv.createUser(user);
		
		return ResponseEntity.status(201).body(created);
	}
	
	/**
	 * 
	 * Method to update user
	 * checks if user exist before update
	 * @param user
	 * @return ResponseEntiy or Exception
	 * @throws ResourceNotFoundException
	 */
	@PutMapping("/users")
	public ResponseEntity<?> updateUser(@Valid @RequestBody User user) throws ResourceNotFoundException{
		
		if( serv.findUserByUsername(user.getUsername()) != null){
			User updated = serv.updateUser(user);
			return ResponseEntity.status(200).body(updated);
		}
		
		throw new ResourceNotFoundException("user with id: "+ user.getUserID()+ " was not found");
	}
	
	/**
	 * Method to remove user by the user_id
	 * @param id
	 * @return ResponseEntity with the status and the message body
	 */
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable int id) {
		if(serv.deleteUser(id) ) {
			return ResponseEntity.status(200).body("user with id: "+ id + " was deleted");
		}
		
		return ResponseEntity.status(400).body("id not found");
	}
	
}