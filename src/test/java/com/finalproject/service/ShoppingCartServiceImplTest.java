package com.finalproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finalproject.dto.LoginDTO;
import com.finalproject.dto.cartitem.ShoppingCartDTO;
import com.finalproject.entity.CartItem;
import com.finalproject.entity.Product;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;
import com.finalproject.exception.ResourceNotFoundException;
import com.finalproject.exception.ShoppingCartException;
import com.finalproject.repository.ShoppingCartRepository;
import com.finalproject.repository.UserRepository;
import com.finalproject.security.JwtAuthenticationFilter;
import com.finalproject.security.JwtTokenProvider;

import mockit.Mocked;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceImplTest {
	
	/*@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private UserRepository userRepository;*/
	

	@Mock
	private ShoppingCartRepository shoppingCartRepository;
	
	@InjectMocks
	private ShoppingCartServiceImpl shoppingCartService;
	
	private ShoppingCart shoppingCart;
	private Product product;
	private CartItem cartItem; 
	private User user;
	
	@Mocked
	HttpServletRequest mockRequest;
	@Mocked
	HttpServletResponse mockResponse;
	
	@BeforeEach
	public void setup() {
		shoppingCart = new ShoppingCart();
		product = new Product();
		cartItem = new CartItem();
		cartItem.setId(1L);
		cartItem.setCreatedDate(new Date());
		cartItem.setProduct(product);
		cartItem.setQuantity(1);
		cartItem.setShoppingCart(shoppingCart);
		Set<CartItem> setCartItems = new HashSet<>();
		
		user = new User();
		user.setId(1L);user.setName("name");
		user.setUsername("username");
		user.setEmail("user@gmail.com");
		user.setPassword("userPassword");
		user.setCurrentAddress("userAddress");
		
		shoppingCart.setAddress(user.getCurrentAddress());
		shoppingCart.setCartItems(setCartItems);
		shoppingCart.setId(1L);
		shoppingCart.setUser(user);
		
	}
	
	
	
	
	@Test
	public void testSaveShoppingCart() {
		//given
		ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
		shoppingCartDTO.setId(1L); shoppingCartDTO.setProductId(1L);
		
		given(shoppingCartRepository.findById(shoppingCart.getId()))
		.willReturn(Optional.empty());
		
		given(shoppingCartRepository.save(shoppingCart))
		.willReturn(shoppingCart);
		
		//when
		//ShoppingCart savedShoppingCart = shoppingCartService.addToShoppingCart(shoppingCartDTO, request)
				//HttpServletRequest
		shoppingCartService.addToShoppingCart(shoppingCartDTO, mockRequest);
		
		//then
	}
	
	//@Test
	void givenHttpServletRequest_whenMockedWithMockito_thenReturnsParameterValues() throws IOException {
	    // mock HttpServletRequest & HttpServletResponse
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);

	    // mock the returned value of request.getParameterMap()
	    when(request.getParameter("firstName")).thenReturn("Mockito");
	    when(request.getParameter("lastName")).thenReturn("Test");
	    //when(response.getWriter()).thenReturn(new PrintWriter(writer));

	    //servlet.doGet(request, response);

	    //assertThat(writer.toString()).isEqualTo("Full Name: Mockito Test");
	}
	
}
