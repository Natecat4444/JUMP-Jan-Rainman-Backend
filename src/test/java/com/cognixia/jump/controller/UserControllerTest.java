package com.cognixia.jump.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.cognixia.jump.model.User;
import com.cognixia.jump.model.User.Role;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	
	private final String STARTING_URI = "http://localhost:8080/api";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userserv;
	
	@MockBean
	UserDetailsService userDetailService;
	
	@InjectMocks
	private UserController usercontroller;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	JwtUtil jwtUtil;
	

	@Test
	@WithMockUser(username = "admin", roles = {"USER","ADMIN"})
	void shouldReturnAllUser() throws Exception {
		String uri = STARTING_URI + "/users";

		List<User> allUser = Arrays.asList(new User());

		when(userserv.getAllUsers()).thenReturn(allUser);

		mvc.perform(get(uri)) // perform request ..
				.andDo(print()) // ..then print request sent and response returned
				.andExpect(status().isOk()) // expect 200 status code
				.andExpect(jsonPath("$.length()").value(allUser.size())); // expected number of elements

		verify(userserv, times(1)).getAllUsers(); // getAllUsers() was used once
		verifyNoMoreInteractions(userserv); // after checking above, check service is no longer being used
	}
	

	@Test
	void shouldCreateUser() throws Exception{
		
		String uri = STARTING_URI + "/users";
		User user = new User();
		
		String json = mapper.writeValueAsString(user);
	
		when(userserv.createUser(Mockito.any(User.class))).thenReturn(user);
		
		mvc.perform(post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
		
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"USER","ADMIN"})
	void shouldUpdateUser() throws Exception {
		String uri = STARTING_URI + "/users";
		User user = new User();
		
		String json = mapper.writeValueAsString(user);
	
		when(userserv.updateUser(Mockito.any(User.class))).thenReturn(user);
		
		mvc.perform(post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
			.andDo(print())
			.andExpect(status().isCreated());
//			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
		
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"USER","ADMIN"})
	void shouldRemoveUser() throws Exception {
		int id = 1;
		String uri = STARTING_URI + "/users/{id}";
		User user = new User(1, "N/A","N/A","user1","pwd123",Role.ROLE_USER, true, null);

		when(userserv.deleteUser(id)).thenReturn(true);

		mvc.perform(delete(uri, id))
			.andDo(print())
			.andExpect(status().isOk());

		verify(userserv, times(1)).deleteUser(id);
		verifyNoMoreInteractions(userserv);
	}
	
	
}
