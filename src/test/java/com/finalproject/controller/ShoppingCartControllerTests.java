package com.finalproject.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.dto.cartitem.ShoppingCartDTO;
import com.finalproject.entity.Category;
import com.finalproject.entity.Product;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;
import com.finalproject.repository.CategoryRepository;
import com.finalproject.repository.ShoppingCartRepository;
import com.finalproject.security.CustomUserDetailsService;
import com.finalproject.security.JwtAuthenticationEntryPoint;
import com.finalproject.security.JwtTokenProvider;
import com.finalproject.service.ProductServiceImpl;
import com.finalproject.service.ShoppingCartServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
//import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;

//@AutoConfigureMockMvc
@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerTests {
	
	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	
	@MockBean
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@MockBean
	private JwtTokenProvider jwtTokenProvider;
	
	
	
	
	@MockBean
	private ShoppingCartServiceImpl shoppingCartService;
	
	@MockBean
	private ShoppingCartRepository shoppingCartRepository;
	
	@MockBean
	private ProductServiceImpl productService;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	
	
	@Autowired
	private MockMvc mockMvc;
	
	private Product product;

	
	@BeforeEach
	void setup() {
		Category category = new Category();
		category.setCategoryName("category name");
		category.setDescription("category description");
		product = new Product();
		product.setCategory(category);
		product.setDescription("description");
		product.setName("name");
		product.setPrice(1);
		
		User user = new User();
		user.setCurrentAddress("Address");
		user.setEmail("email@email.com");
		user.setId(1L);
		user.setName("username");
		user.setPassword("userPassword");
		
	}
	
	
	@Test
	void shouldCreateMockMvc() {
		assertNotNull(mockMvc);
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void shouldReturnListOfItems() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		when(shoppingCartService.listCartItems(request))
		.thenReturn(null);
		
		this.mockMvc.perform(MockMvcRequestBuilders
			.get("http://localhost:8087/api/shoppingcart"))
		.andExpect(MockMvcResultMatchers.status().isOk());
		//.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(true)));
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void shouldAddItemToShoppingCart() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		//when(productService.getById(anyLong())).thenReturn(product);
		//when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(null);
		
		ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
		shoppingCartDTO.setProductId(product.getId());
		shoppingCartDTO.setId(2L);
		
		ShoppingCart shoppingCart = new ShoppingCart();
		when(shoppingCartService.addToShoppingCart(any(ShoppingCartDTO.class), any(HttpServletRequest.class)))
		.thenReturn(shoppingCart);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8087/api/shoppingcart")
			.content(new ObjectMapper().writeValueAsString(shoppingCart))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isCreated());
			//.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(true)));
		
		/*
		this.mockMVC.perform(post("http://localhost:8085/user")
				.content(new ObjectMapper().writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.email").value("a@b.com"))
				.andExpect(jsonPath("$.firstName").value("seb"))
				.andExpect(jsonPath("$.lastName").value("sil"))
				.andExpect(jsonPath("$.phoneNumber").value("+503123456789"));*/
	}
	
	
}
