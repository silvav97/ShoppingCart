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
import com.finalproject.entity.Role;
import com.finalproject.entity.User;
import com.finalproject.repository.RoleRepository;
import com.finalproject.repository.UserRepository;
import com.finalproject.security.JwtAuthResponseDTO;
import com.finalproject.security.JwtTokenProvider;
import com.finalproject.service.AddressServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private AddressServiceImpl addressService;
	
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
		if(userRepository.existsByUsername(signupDTO.getUsername())) {
			return new ResponseEntity<>("That username already exists", HttpStatus.BAD_REQUEST);
		}
		if(userRepository.existsByEmail(signupDTO.getEmail())) {
			return new ResponseEntity<>("That email already exists", HttpStatus.BAD_REQUEST);
		}
		User user = new User();
		user.setName(signupDTO.getName());
		user.setUsername(signupDTO.getUsername());
		user.setEmail(signupDTO.getEmail());
		user.addAddress(addressService.saveAddress(signupDTO.getAddress()));
		user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
		
		Role role = roleRepository.findByName("ROLE_USER").get();
		user.setRoles(Collections.singleton(role));
		
		userRepository.save(user);
		return new ResponseEntity<>("User registerd successfully", HttpStatus.OK);
	}
	

}
