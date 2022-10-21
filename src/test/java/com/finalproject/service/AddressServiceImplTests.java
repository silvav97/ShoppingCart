package com.finalproject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finalproject.entity.Address;
import com.finalproject.repository.AddressRepository;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTests {
	
	
	@Mock
	private AddressRepository addressRepository;
	
	@InjectMocks
	private AddressServiceImpl addressService;
	
	@Test
	void shouldWork() {
		assertEquals(0, 0);
	}
	
	@Test
	public void GIVEN_THEN_Save_Address() {
		// given
		Address address = new Address();
		
		// when
		when(addressRepository.save(any(Address.class))).thenReturn(address);
		
		// then
		Address savedAddress = addressService.saveAddress("this is an address");
		assertThat(savedAddress).isNotNull();
	}

}
