package com.finalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.entity.Address;
import com.finalproject.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public Address saveAddress(String address) {
		Address newAddress = new Address();
		newAddress.setUserAddres(address);
		
		return addressRepository.save(newAddress);
	}

}
