package com.finalproject.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalproject.dto.LoginDTO;
import com.finalproject.dto.SignupDTO;
import com.finalproject.entity.Address;
import com.finalproject.entity.Role;
import com.finalproject.entity.User;
import com.finalproject.repository.RoleRepository;
import com.finalproject.repository.UserRepository;
import com.finalproject.security.JwtAuthResponseDTO;
import com.finalproject.security.JwtTokenProvider;
import com.finalproject.service.AddressServiceImpl;
import com.finalproject.service.PaymentMethodServiceImpl;
import com.finalproject.service.UserServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserServiceImpl userService;
	
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// Get the token from jwtTokenProvider
		String token = jwtTokenProvider.generateToken(authentication);
		return new ResponseEntity<>(new JwtAuthResponseDTO(token) ,HttpStatus.OK);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@RequestBody SignupDTO signupDTO) {
		userService.save(signupDTO);
		return new ResponseEntity<>("User registerd successfully", HttpStatus.OK);
	}
	

}
