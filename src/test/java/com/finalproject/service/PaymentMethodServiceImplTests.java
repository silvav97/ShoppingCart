package com.finalproject.service;



import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.*;


import com.finalproject.entity.Address;
import com.finalproject.entity.PaymentMethod;
import com.finalproject.repository.PaymentMethodRepository;


@ExtendWith(MockitoExtension.class)
public class PaymentMethodServiceImplTests {
	
	@Mock
	private PaymentMethodRepository paymentMethodRepository;
	
	@InjectMocks
	private PaymentMethodServiceImpl paymentMethodService;
	
	@Test
	void shouldWork() {
		assertEquals(0, 0);
	}
	
	@Test
	public void saveAddress() {
		// given
		List<PaymentMethod> list = new ArrayList<>();
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setId(1L);
		paymentMethod.setPaymentMethod("1");
		list.add(paymentMethod);
		
		// when
		when(paymentMethodRepository.findAll()).thenReturn(list);
		
		// then
		PaymentMethod payment = paymentMethodService.getDefaultPaymentMethod();
		assertThat(payment).isNotNull();
	}

}
