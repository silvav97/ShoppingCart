package com.finalproject.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import com.finalproject.entity.User;
import com.finalproject.exception.ResourceNotFoundException;
import com.finalproject.exception.ShoppingCartException;
import com.finalproject.repository.UserRepository;


public class GetUserFromRequest {
	@Autowired
	private static JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private static JwtTokenProvider jwtTokenProvider;

	@Autowired
	private static UserRepository userRepository;
	
	// Method to get the user
	public static User getTheUserFromRequest(HttpServletRequest request) {
		User user = new User();
		String username;
		String token = jwtAuthenticationFilter.getJwtFromRequest(request);
		// Validate the token
		if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			// Get the User
			username = jwtTokenProvider.getUsernameFromJWT(token);
			user = userRepository.findByUsernameOrEmail(username, username)
					.orElseThrow(() -> new ResourceNotFoundException("User", "username or Email", 1L));
			return user;
		}
		throw new ShoppingCartException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid token");
	}
	
}
