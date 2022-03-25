package com.cognixia.jump.model;


import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;

@Document("users")
public class User {
	
	public static enum Role {
		ROLE_USER, ROLE_ADMIN // roles need to be capital and start with ROLE_
	}
	
	
	
	@Schema(description = "ID for User",
	example = "1", 
	required = true)
	@Id
	private String userID;
	
	@Schema(description = "first name of user",
		example="Matthew",
		required= true)
	private String first_name;
	
	@Schema(description = "last name of user",
			example="TrueLove",
			required= true)
	private String last_name;
	
	@Schema(description = "Accounts username for user",
			example = "user1", 
			required = true)
	
	@NotNull(message = "userName cannot be null")
	private String username;
	
	@Schema(description = "Accounts password for user",
			example = "password", 
			required = true)
	
	@NotNull(message = "Password cannot be null")
	private String password;
	
	@Schema(description = "Role of current user",
			example = "ROLE_USER", 
			required = true)
	@NotNull(message = "Role cannot be null")
	private Role role;
	
	private String importrole;
	
	
	@Schema(description = "checks if user is active acount",
			example = "true", 
			required = true)
	private boolean enabled;
	
	@Schema(description = "credits of the user",
			example = "100", 
			required = true)
	private Integer credit;
	
	public User() {
		this(null,"N/A", "N/A" ,"N/A", "N/A", Role.ROLE_USER, true, null);
	}

	public User(String userID, String first_name, String last_name,
			@NotNull(message = "userName cannot be null") String username,
			@NotNull(message = "Password cannot be null") String password,
			@NotNull(message = "Role cannot be null") Role role, boolean enabled, Integer credit) {
		super();
		this.userID = userID;
		this.first_name = first_name;
		this.last_name = last_name;
		this.username = username;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.credit = credit;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	
	
}
