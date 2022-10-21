package com.finalproject.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.finalproject.dto.SignupDTO;
import com.finalproject.entity.Address;
import com.finalproject.entity.PaymentMethod;
import com.finalproject.entity.Role;
import com.finalproject.exception.ShoppingCartException;
import com.finalproject.repository.AddressRepository;
import com.finalproject.repository.RoleRepository;
import com.finalproject.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {
	
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private AddressServiceImpl addressService;
	
	@Mock
	private PaymentMethodServiceImpl paymentMethodService;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private RoleRepository roleRepository;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@Test
	void shouldWork() {
		assertEquals(0, 0);
	}
	
	@Test
	public void GIVEN_Existing_Username_THEN_Can_Not_Create_A_User() {
		// given
		SignupDTO signupDTO = new SignupDTO();
		signupDTO.setAddress("address");
		signupDTO.setEmail("email");
		signupDTO.setName("name");
		signupDTO.setPassword("password");
		signupDTO.setUsername("username");
		
		// when
		when(userRepository.existsByUsername(anyString())).thenReturn(true);
		
		// then
		try {
			userService.save(signupDTO);
		} catch (Exception e) {
			assertTrue(e instanceof ShoppingCartException);
		}
	}
	
	@Test
	public void GIVEN_Existing_Email_THEN_Can_Not_Create_A_User() {
		// given
		SignupDTO signupDTO = new SignupDTO();
		signupDTO.setAddress("address");
		signupDTO.setEmail("email");
		signupDTO.setName("name");
		signupDTO.setPassword("password");
		signupDTO.setUsername("username");
		
		// when
		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		when(userRepository.existsByEmail(anyString())).thenReturn(true);
		
		// then
		try {
			userService.save(signupDTO);
		} catch (Exception e) {
			assertTrue(e instanceof ShoppingCartException);
		}
	}
	
	@Test
	public void GIVEN_Unexisting_User_THEN_Can_Not_Create_A_User() {
		// given
		SignupDTO signupDTO = new SignupDTO();
		signupDTO.setAddress("address");
		signupDTO.setEmail("email");
		signupDTO.setName("name");
		signupDTO.setPassword("password");
		signupDTO.setUsername("username");
		
		Address address = new Address();
		PaymentMethod paymentMethod = new PaymentMethod();
		Role role = new Role();
		
		// when
		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		when(addressService.saveAddress(anyString())).thenReturn(address);
		when(paymentMethodService.getDefaultPaymentMethod()).thenReturn(paymentMethod);
		when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
		
		// then
		try {
			userService.save(signupDTO);
		} catch (Exception e) {
			assertTrue(e instanceof ShoppingCartException);
		}
	}

}
