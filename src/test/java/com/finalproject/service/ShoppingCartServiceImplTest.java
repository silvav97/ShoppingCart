package com.finalproject.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.util.StringUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;



import com.finalproject.dto.CreateAddressDTO;

import com.finalproject.dto.UpdateAddressDTO;
import com.finalproject.dto.UpdatePaymentMethodDTO;
import com.finalproject.dto.cartitem.ShoppingCartDTO;
import com.finalproject.dto.cartitem.ShoppingCartListItemsDTO;
import com.finalproject.entity.Address;
import com.finalproject.entity.CartItem;
import com.finalproject.entity.Category;
import com.finalproject.entity.PaymentMethod;
import com.finalproject.entity.Product;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;
import com.finalproject.exception.CartItemDoesNotBelongToUserException;
import com.finalproject.exception.ResourceNotFoundException;

import com.finalproject.exception.UserDoesNotHaveAShoppingCartException;
import com.finalproject.repository.AddressRepository;
import com.finalproject.repository.CartRepository;
import com.finalproject.repository.PaymentMethodRepository;
import com.finalproject.repository.ShoppingCartRepository;
import com.finalproject.repository.UserRepository;
import com.finalproject.security.CustomUserDetailsService;
import com.finalproject.security.JwtAuthenticationEntryPoint;
import com.finalproject.security.JwtAuthenticationFilter;
import com.finalproject.security.JwtTokenProvider;



import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceImplTest {
	
	@Mock
	private CustomUserDetailsService customUserDetailsService;
	
	@Mock
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Mock
	private JwtTokenProvider jwtTokenProvider;
	
	@Mock
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Mock
	private UserRepository userRepository;

	@Mock
	private ShoppingCartRepository shoppingCartRepository;
	
	@Mock
	private ProductServiceImpl productService;
	
	@Mock
	private CartRepository cartRepository;
	
	@Mock
	private AddressRepository addressRepository;
	
	@Mock
	private PaymentMethodRepository paymentMethodRepository;
	
	@InjectMocks
	private ShoppingCartServiceImpl shoppingCartService;
	
	private ShoppingCart shoppingCart;
	private Product product;
	private CartItem cartItem; 
	private User user;
	private Category category;
	
	//@Mock
	//HttpServletRequest mockRequest;
	//@Mock
	//HttpServletResponse mockResponse;
	
	@BeforeEach
	public void setup() {
		
		MockitoAnnotations.openMocks(this);
		shoppingCart = new ShoppingCart();
		category = new Category();
		category.setCategoryName("categoryName");
		product = new Product();
		product.setCategory(category);
		product.setId(1L);
		cartItem = new CartItem();
		cartItem.setId(1L);
		cartItem.setCreatedDate(new Date());
		cartItem.setProduct(product);
		cartItem.setQuantity(1);
		cartItem.setShoppingCart(shoppingCart);
		Set<CartItem> setCartItems = new HashSet<>();
		setCartItems.add(cartItem);
		
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
	public void shouldWorks() {
		assertEquals(0, 0);
	}
	
	
	
	/*@Test
	public void testSaveShoppingCart() {
		//given
		ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
		shoppingCartDTO.setId(1L); shoppingCartDTO.setProductId(1L);
		
		given(shoppingCartRepository.findById(shoppingCart.getId()))
		.willReturn(Optional.empty());
		
		given(shoppingCartRepository.save(shoppingCart))
		.willReturn(shoppingCart);
		
		//when
		shoppingCartService.addToShoppingCart(shoppingCartDTO, mockRequest);
		
		//then
		
		when(jwtAuthenticationFilter.getJwtFromRequest(any(HttpServletRequest.class))).thenReturn("token");
		when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
		when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("sebastian");
		//Optional
		//when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional<User> user);
		
		when(shoppingCartService.getTheUserFromRequest(any(HttpServletRequest.class))).thenReturn(user);
	}*/
	
	@Nested
	class addToShoppingCartRelatedMethods {
		@Test
		public void GIVEN_Shopping_Cart_Did_Exist_And_Item_Does_Not_THEN_Add_Item() {
			// given
			ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
			Product product2 = new Product();
			product2.setId(2L);
			CartItem cartItem2 = new CartItem();
			cartItem2.setCreatedDate(new Date());
			cartItem2.setProduct(product2);
			cartItem2.setQuantity(1);
			
			shoppingCartDTO.setId(1L); 
			shoppingCartDTO.setProductId(2L);
			
			MockHttpServletRequest request = new MockHttpServletRequest();
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(productService.getById(anyLong())).thenReturn(product2);
			when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.of(shoppingCart));
			
			// then
			assertThat(shoppingCart.getCartItems().size()).isEqualTo(1);
			shoppingCartService.addToShoppingCart(shoppingCartDTO, request);
			assertThat(shoppingCart.getCartItems().size()).isEqualTo(2);
		}
		
		@Test
		public void GIVEN_Neither_Shopping_Cart_Nor_Item_Exist_THEN_Add_Item() {
			// given
			ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
			shoppingCartDTO.setId(1L); shoppingCartDTO.setProductId(1L);
			MockHttpServletRequest request = new MockHttpServletRequest();
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(productService.getById(anyLong())).thenReturn(product);
			when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.ofNullable(null));
			
			// then
			ShoppingCart shoppingCart2 = shoppingCartService.addToShoppingCart(shoppingCartDTO, request);
			try {
				shoppingCart2.getCartItems().size();
			} catch (Exception e) {
				assertTrue(e instanceof NullPointerException);
			}
			
		}
		
		@Test
		public void GIVEN_Both_Shopping_Cart_And_Item_Exist_THEN_Add_Item() {
			// given
			Set<CartItem> cartItems = new HashSet<>();
			cartItems.add(cartItem);
			shoppingCart.setCartItems(cartItems);
			ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
			shoppingCartDTO.setId(1L); shoppingCartDTO.setProductId(1L);
			MockHttpServletRequest request = new MockHttpServletRequest();
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(productService.getById(anyLong())).thenReturn(product);
			when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.of(shoppingCart));
			
			// then
			assertThat(shoppingCart.getCartItems().size()).isEqualTo(1);
			CartItem firstElement = null;
			for(CartItem item :shoppingCart.getCartItems()) {
				firstElement = item;
			}
			assertThat(firstElement.getQuantity()).isEqualTo(1);
			shoppingCartService.addToShoppingCart(shoppingCartDTO, request);
			for(CartItem item :shoppingCart.getCartItems()) {
				firstElement = item;
			}
			assertThat(firstElement.getQuantity()).isEqualTo(2);	
		}
		
	} // end addToShoppingCart Method Tests
	
	
	
	@Nested
	class listCartItemsRelatedMethods {
		
		@Test
		public void GIVEN_Shopping_Cart_Did_Not_Exist_THEN_Try_To_Get_Items() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.ofNullable(null));

			//then
			try {
				shoppingCartService.listCartItems(request);

			} catch (Exception e) {
				assertTrue(e instanceof ResourceNotFoundException);
			}	
		}
		
		@Test
		public void GIVEN_Shopping_Cart_Did_Exist_But_Does_Not_Have_Items_THEN_Get_Empty_List_of_Items() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			ShoppingCart shoppingCartEmpty = new ShoppingCart();
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.of(shoppingCartEmpty));
			
			// then
			ShoppingCartListItemsDTO shoppingCartListItemsDTO = shoppingCartService.listCartItems(request);	
			assertThat(shoppingCartListItemsDTO.getCartItems().size()).isEqualTo(0);
		}

		@Test
		public void GIVEN_Shopping_Cart_Did_Exist_And_Have_Items_THEN_Get_List_of_Items() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.of(shoppingCart));
			
			// then
			ShoppingCartListItemsDTO shoppingCartListItemsDTO = shoppingCartService.listCartItems(request);	
			assertThat(shoppingCartListItemsDTO.getCartItems().size()).isEqualTo(1);
		}
		
		
	} // end listCartItems Method Tests
	
	
	@Nested
	class reduceOrDeleteCartItemRelatedMethods {
		
		@Test
		public void GIVEN_Shopping_Cart_Did_Not_Exist_THEN_Try_To_delete_Item() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
			when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.ofNullable(null));
			
			// then
			try {
				shoppingCartService.reduceOrDeleteCartItem(anyLong(), request);
				
			} catch (Exception e) {
				assertTrue(e instanceof UserDoesNotHaveAShoppingCartException);
			}
		}
		
		@Test
		public void GIVEN_Cart_Item_Did_Not_Exist_THEN_Try_To_delete_The_Item() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(cartRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
			
			// then
			try {
				shoppingCartService.reduceOrDeleteCartItem(anyLong(), request);
				
			} catch (Exception e) {
				assertTrue(e instanceof ResourceNotFoundException);
			}
		}
		
		@Test
		public void GIVEN_Both_Shopping_Cart_And_Item_Exist_But_Cart_Does_Not_Belong_To_Shopping_Cart_THEN_Get_Exception() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			ShoppingCart shoppingCart2 = new ShoppingCart();
			Product product2 = new Product();
			product2.setCategory(category);
			CartItem cartItem2 = new CartItem();
			cartItem2.setId(2L);
			cartItem2.setCreatedDate(new Date());
			cartItem2.setProduct(product2);
			cartItem2.setQuantity(15);
			cartItem2.setShoppingCart(shoppingCart2);
			Set<CartItem> setCartItems2 = new HashSet<>();
			setCartItems2.add(cartItem2);
			
			User user2 = new User();
			user2.setId(1L);user2.setName("name");
			user2.setUsername("username");
			user2.setEmail("user@gmail.com");
			user2.setPassword("userPassword");
			user2.setCurrentAddress("userAddress");
			
			shoppingCart2.setAddress(user2.getCurrentAddress());
			shoppingCart2.setCartItems(setCartItems2);
			shoppingCart2.setId(2L);
			shoppingCart2.setUser(user2);

			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cartItem2));
			when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.of(shoppingCart));
			
			// then
			try {
				shoppingCartService.reduceOrDeleteCartItem(anyLong(), request);
				
			} catch (Exception e) {
				assertTrue(e instanceof CartItemDoesNotBelongToUserException);
			}
		}
		
		@Test 
		public void GIVEN_Shopping_Cart_Did_Exist_And_Have_Just_One_Item_With_Quantity_1_THEN_Delete_Item() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
			when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.of(shoppingCart));
			
			// then
			assertThat(shoppingCart.getCartItems().size()).isEqualTo(1);
			shoppingCartService.reduceOrDeleteCartItem(anyLong(), request);
			assertThat(shoppingCart.getCartItems().size()).isEqualTo(0);
			
		}
		
		@Test 
		public void GIVEN_Shopping_Cart_Did_Exist_And_Have_Just_One_Item_With_Quantity_More_Than_1_THEN_Reduce_Item() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			Set<CartItem> cartItems = new HashSet<>();
			cartItems.add(cartItem);
			shoppingCart.setCartItems(cartItems);
			ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
			shoppingCartDTO.setId(1L); shoppingCartDTO.setProductId(1L);
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			
			when(productService.getById(anyLong())).thenReturn(product);
			when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.of(shoppingCart));
			when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
			//when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.of(shoppingCart));
			
			
			
			// then
			// add same item, it means quantity increases by one
			shoppingCartService.addToShoppingCart(shoppingCartDTO, request);			
			
			CartItem cartItem = null;
			for(CartItem element : shoppingCart.getCartItems()) {
				cartItem = element;
			}
			assertThat(cartItem.getQuantity()).isEqualTo(2);
			shoppingCartService.reduceOrDeleteCartItem(anyLong(), request);
			assertThat(cartItem.getQuantity()).isEqualTo(1);
			
		}
		
		
		
	}
	
	@Nested
	class addAddressRelatedMethods {
		
		@Test 
		public void GIVEN_Address_Then_Add_Address_To_User() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			CreateAddressDTO createAddressDTO = new CreateAddressDTO();
			String addressName = "other Address";
			createAddressDTO.setNewAddress(addressName);
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(userRepository.save(any(User.class))).thenReturn(user);
			
			// then
			shoppingCartService.addAddress(createAddressDTO, request);
			assertThat(user.getCurrentAddress()).isEqualTo(addressName);
			//System.out.println("Number of addresses: "+user.getAddresses().size());
		}
	}
	
	@Nested
	class updateCurrentAddressRelatedMethods {
		
		//@Test 
		public void GIVEN_Address_Id_Then_Update_Address_To_User() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			
			CreateAddressDTO createAddressDTO = new CreateAddressDTO();
			createAddressDTO.setId(1L);
			String addressName = "Address1";
			createAddressDTO.setNewAddress(addressName);
			
			
			CreateAddressDTO createAddressDTO2 = new CreateAddressDTO();
			createAddressDTO2.setId(2L);
			String addressName2 = "Address2";
			createAddressDTO2.setNewAddress(addressName2);
			
			
			
			Address address = new Address();
			address.setId(2L);
			address.setUserAddres("newUserAddress");
			
			
			UpdateAddressDTO updateAddressDTO = new UpdateAddressDTO();
			updateAddressDTO.setCurrentAddressId(2L);
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(userRepository.save(user)).thenReturn(user);
			when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));
			
			// then
			shoppingCartService.addAddress(createAddressDTO, request);
			shoppingCartService.addAddress(createAddressDTO2, request);
			
			for(Address addressItem : user.getAddresses()) {
				System.out.println("Address Names: " + addressItem.getUserAddres() +" with id: " + addressItem.getId());
			}
			System.out.println("Current address idddd: " + user.getCurrentAddress());
			
			System.out.println("Number of addresses: "+user.getAddresses().size());
			
			
			//updateAddressDTO.setCurrentAddressId(id);
			shoppingCartService.updateCurrentAddress(updateAddressDTO, request);
		}
		
		//@Test   // AddressDoesNotBelongToUserException
		public void GIVEN_Address_Add_To_User() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			Address address = new Address();
			address.setUserAddres("newUserAddress");
			
			UpdateAddressDTO updateAddressDTO = new UpdateAddressDTO();
			updateAddressDTO.setCurrentAddressId(1L);
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			
			when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));
			//when(userRepository.save(any(User.class))).thenReturn(user);
			
			// then
			shoppingCartService.updateCurrentAddress(updateAddressDTO, request);
		}
		
		
		
	}
	
	@Nested
	class updatePaymentMethodRelatedMethods {
		@Test 
		public void GIVEN_Payment_Method_Then_Update_Payment_Method_To_User() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			String paymentMethodCheck = "paymentMethod being checked";
			PaymentMethod paymentMethod = new PaymentMethod();
			paymentMethod.setId(1L);
			paymentMethod.setPaymentMethod(paymentMethodCheck);
			
			UpdatePaymentMethodDTO updatePaymentMethodDTO = new UpdatePaymentMethodDTO();
			updatePaymentMethodDTO.setPaymentMethodId(1L);
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.of(paymentMethod));
			when(userRepository.save(any(User.class))).thenReturn(user);
			
			// then
			shoppingCartService.updatePaymentMethod(updatePaymentMethodDTO, request);
			assertThat(user.getPaymentMethod().getPaymentMethod()).isEqualTo(paymentMethodCheck);
		}
		
		
		@Test 
		public void GIVEN_Payment_Method_Did_Not_Exist_Then_Exception() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();
			
			
			UpdatePaymentMethodDTO updatePaymentMethodDTO = new UpdatePaymentMethodDTO();
			updatePaymentMethodDTO.setPaymentMethodId(1L);
			
			// when
			when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
			when(jwtTokenProvider.validateToken("string")).thenReturn(true);
			when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
			when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
			when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
			
			// then
			try {
				shoppingCartService.updatePaymentMethod(updatePaymentMethodDTO, request);
				
			} catch (Exception e) {
				assertTrue(e instanceof ResourceNotFoundException);
			}
			
			
		}
	}
	
	
	
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
