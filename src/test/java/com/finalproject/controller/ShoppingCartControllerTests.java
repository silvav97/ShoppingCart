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

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.common.ApiResponse;
import com.finalproject.dto.CreateAddressDTO;
import com.finalproject.dto.ProductDTO;
import com.finalproject.dto.UpdateAddressDTO;
import com.finalproject.dto.UpdatePaymentMethodDTO;
import com.finalproject.dto.cartitem.CartItemDTO;
import com.finalproject.dto.cartitem.ShoppingCartDTO;
import com.finalproject.dto.cartitem.ShoppingCartListItemsDTO;
import com.finalproject.entity.CartItem;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private ProductServiceImpl productService;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	private Category category;
	private Product product;
	private User user;
	private CartItem cartItem;
	private ShoppingCart shoppingCart;

	
	@BeforeEach
	void setup() {
		category = new Category();
		category.setCategoryName("category name");
		category.setDescription("category description");
		product = new Product();
		product.setCategory(category);
		product.setDescription("description");
		product.setName("name");
		product.setPrice(1);
		
		user = new User();
		user.setCurrentAddress("sebastianAddress");
		user.setEmail("sebastian@gmail.com");
		user.setId(1L);
		user.setName("sebastian");
		user.setPassword("sebastianPassword");
		
		shoppingCart = new ShoppingCart(); 
		
		cartItem = new CartItem();
		cartItem.setCreatedDate(new Date());
		cartItem.setId(1L);
		cartItem.setProduct(product);
		cartItem.setQuantity(10);
		cartItem.setShoppingCart(shoppingCart);
		
	}
	
	
	@Test
	void shouldCreateMockMvc() {
		assertNotNull(mockMvc);
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void shouldReturnListOfItems() throws Exception {
		// given
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(1L);
		productDTO.setCategoryName(category.getCategoryName());
		productDTO.setDescription(product.getDescription());
		productDTO.setName(product.getName());
		productDTO.setPrice(product.getPrice());
		
		CartItemDTO cartItemDTO = new CartItemDTO();
		cartItemDTO.setId(1L);
		cartItemDTO.setProductDTO(productDTO);
		cartItemDTO.setQuantity(cartItem.getQuantity());
	
		
		List<CartItemDTO> cartItems = new ArrayList<>();
		cartItems.add(cartItemDTO);
		
		ShoppingCartListItemsDTO shoppingCartListItemsDTO = new ShoppingCartListItemsDTO();
		shoppingCartListItemsDTO.setCartItems(cartItems);
		shoppingCartListItemsDTO.setUserName(user.getName());
		shoppingCartListItemsDTO.setUserEmail(user.getEmail());
		shoppingCartListItemsDTO.setDeliveryAddress(user.getCurrentAddress());
		
		//when
		when(shoppingCartService.listCartItems(any(HttpServletRequest.class)))
		.thenReturn(shoppingCartListItemsDTO);
		
		//then
		this.mockMvc.perform(get("http://localhost:8087/api/shoppingcart")
			.content(new ObjectMapper().writeValueAsString(shoppingCart))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.userName").value("sebastian"))
			.andExpect(jsonPath("$.userEmail").value("sebastian@gmail.com"))
			.andExpect(jsonPath("$.deliveryAddress").value("sebastianAddress"))
			.andExpect(jsonPath("$.cartItems[0].id").value(1L))
			.andExpect(jsonPath("$.cartItems[0].quantity").value(10))
			.andExpect(status().isOk());
		
		
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void shouldAddItemToShoppingCart() throws Exception {
		// given
		ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
		shoppingCartDTO.setProductId(product.getId());
		shoppingCartDTO.setId(2L);
		ShoppingCart shoppingCart = new ShoppingCart();
		
		// when
		when(shoppingCartService.addToShoppingCart(any(ShoppingCartDTO.class), any(HttpServletRequest.class)))
		.thenReturn(shoppingCart);
		
		// then
		this.mockMvc.perform(post("http://localhost:8087/api/shoppingcart")
			.content(new ObjectMapper().writeValueAsString(shoppingCart))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("Added to cart"));
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	public void ShouldReduceOrDeleteCartITemTest() throws Exception {
		// when
		doNothing().when(shoppingCartService).reduceOrDeleteCartItem(anyLong(), any(HttpServletRequest.class));
		
		// then
		this.mockMvc.perform(delete("http://localhost:8087/api/shoppingcart/25")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Item has been removed"));
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void shouldAddAddressToUser() throws Exception {
		// when
		doNothing().when(shoppingCartService).addAddress(any(CreateAddressDTO.class), any(HttpServletRequest.class));
		
		// then
		this.mockMvc.perform(post("http://localhost:8087/api/shoppingcart/address")
			.content("{\"newAddres\": \"sebs\"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("Address Added"));

	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void shouldUpdateCurrentAddressToUser() throws Exception {
		// when
		when(shoppingCartService.updateCurrentAddress(any(UpdateAddressDTO.class), any(HttpServletRequest.class))).thenReturn(user);
		
		// then
		this.mockMvc.perform(put("http://localhost:8087/api/shoppingcart/address")
			.content("{\"currentAddressId\": \"7\"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("Current Address Updated"));
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void shouldUpdatePaymentMethodToUser() throws Exception {
		// when
		doNothing().when(shoppingCartService).updatePaymentMethod(any(UpdatePaymentMethodDTO.class), any(HttpServletRequest.class));
		
		// then
		this.mockMvc.perform(put("http://localhost:8087/api/shoppingcart/paymentmethod")
			.content("{\"paymentMethodId\": \"2\"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("Payment Method Updated"));	
	}
	
	
}
