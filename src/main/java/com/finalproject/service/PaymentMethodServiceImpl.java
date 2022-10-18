package com.finalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.entity.PaymentMethod;
import com.finalproject.repository.PaymentMethodRepository;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
	
	@Autowired PaymentMethodRepository paymentMethodRepository;

	@Override
	public PaymentMethod getDefaultPaymentMethod() {
		// returns the first payment method as default
		return paymentMethodRepository.findAll().get(0);
	}

}
