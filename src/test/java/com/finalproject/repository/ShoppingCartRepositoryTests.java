package com.finalproject.repository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.finalproject.entity.CartItem;
import com.finalproject.entity.Product;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTests {

	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	private ShoppingCart shoppingCart;
	private Product product;
	private CartItem cartItem; 
	private User user;
	
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
	
	
	@DisplayName("Test to save a shopping cart")
	@Test
	public void saveShoppingCart() {
		//given
		
		//when
		ShoppingCart savedshoppingCart = shoppingCartRepository.save(shoppingCart);
				
		//then
		assertThat(savedshoppingCart).isNotNull();
		assertThat(savedshoppingCart.getId()).isGreaterThan(0);
	}
	
	@Test
	public void listShoppingCarts() {
		//given
		ShoppingCart otherShoppingCart = new ShoppingCart();
		shoppingCartRepository.save(shoppingCart);
		shoppingCartRepository.save(otherShoppingCart);

		
		//when
		List<ShoppingCart> listShoppingCart = shoppingCartRepository.findAll();
		
		//then
		assertThat(listShoppingCart).isNotNull();
		assertThat(listShoppingCart.size()).isEqualTo(2);
			
		
	}
	
}
