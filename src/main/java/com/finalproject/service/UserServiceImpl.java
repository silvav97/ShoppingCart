package com.finalproject.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finalproject.dto.SignupDTO;
import com.finalproject.entity.Address;
import com.finalproject.entity.Role;
import com.finalproject.entity.User;
import com.finalproject.exception.ShoppingCartException;
import com.finalproject.repository.RoleRepository;
import com.finalproject.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
	private AddressServiceImpl addressService;
	
	@Autowired
	private PaymentMethodServiceImpl paymentMethodService;
	
	@Override
	public void save(SignupDTO signupDTO) {
		if(userRepository.existsByUsername(signupDTO.getUsername())) {
			//return new ResponseEntity<>("That username already exists", HttpStatus.BAD_REQUEST);
			throw new ShoppingCartException(HttpStatus.BAD_REQUEST, "That username already exists");
		}
		if(userRepository.existsByEmail(signupDTO.getEmail())) {
			//return new ResponseEntity<>("That email already exists", HttpStatus.BAD_REQUEST);
			throw new ShoppingCartException(HttpStatus.BAD_REQUEST, "That email already exists");
		}
		User user = new User();
		user.setName(signupDTO.getName());
		user.setUsername(signupDTO.getUsername());
		user.setEmail(signupDTO.getEmail());
		Address address = addressService.saveAddress(signupDTO.getAddress());
		user.setDefaultAddress(address.getUserAddres());
		user.addAddress(address);
		user.setPaymentMethod(paymentMethodService.getDefaultPaymentMethod());
		user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
		
		Role role = roleRepository.findByName("ROLE_USER").get();
		user.setRoles(Collections.singleton(role));
		
		userRepository.save(user);
	}

}
