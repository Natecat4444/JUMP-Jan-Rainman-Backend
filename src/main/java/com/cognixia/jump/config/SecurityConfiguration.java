package com.cognixia.jump.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.cognixia.jump.filter.JwtRequestFilter;

// this class will detail how spring security is going to handle authorization and authentication
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	
	// handle the Authentication( who are you?)
	// lookup if the credentials ( username and password) passed through the request match any of the 
	// users for this service
	@Override
	protected void configure( AuthenticationManagerBuilder auth) throws Exception {
		
		// only look to load the one in-memory user we have(user1)
		auth.userDetailsService(userDetailsService);


	}
	
	// Authorization ( what do you want)
	// which user have access to which uris (APIs)
	@Override
	protected void configure( HttpSecurity http) throws Exception{
		
		http.cors();
		
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers( HttpMethod.GET, "/v3/api-docs/").permitAll()
			.antMatchers( HttpMethod.POST, "/api/users").permitAll()
			.antMatchers( HttpMethod.GET, "/api/bets").hasRole("USER")
			.antMatchers( HttpMethod.POST, "/api/bets").hasRole("USER")
			.antMatchers( HttpMethod.GET, "/api/users").hasRole("ADMIN")
			.antMatchers("/api/authenticate").permitAll() // permit anyone to create a token as long as they are valid users
			.anyRequest().authenticated() // any request to any of out api needs to be authenticated token or users info
			.and().sessionManagement()
			.sessionCreationPolicy( SessionCreationPolicy.STATELESS);
		
		// make sure jwt filter is checked first before any other filter, especially before the filter that checks for the correct
		// username and password of a user
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	}
	
	// mainly used to decode passwords
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		// create no password encoder, old way of setting it up, still works just deprecated
		return new BCryptPasswordEncoder();
	
	}
	
	//method will provide spring security an object ( AuthenticationManger) that can be used to authenticate
	// users accessing the APIs in our service
	// also mark it with @Bean so it can be loaded from the Spring Context and we can have dependency injection wtihin 
	@Bean 
	public AuthenticationManager authenticationManagerBean() throws Exception{
		
		return super.authenticationManagerBean();
	}
	

	
}