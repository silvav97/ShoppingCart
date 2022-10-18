package com.finalproject.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.finalproject.dto.cartitem.CartItemDTO;
import com.finalproject.dto.cartitem.ShoppingCartDTO;
import com.finalproject.dto.cartitem.ShoppingCartListItemsDTO;
import com.finalproject.entity.CartItem;
import com.finalproject.entity.Product;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;
import com.finalproject.exception.CartItemDoesNotBelongToUserException;
import com.finalproject.exception.ResourceNotFoundException;
import com.finalproject.exception.ShoppingCartException;
import com.finalproject.repository.CartRepository;
import com.finalproject.repository.ShoppingCartRepository;
import com.finalproject.repository.UserRepository;
import com.finalproject.security.JwtAuthenticationFilter;
import com.finalproject.security.JwtTokenProvider;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductServiceImpl productService;
	
	// CartRepository ??  -> Yes
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	
	// Method to get the user
	private User getTheUserFromRequest(HttpServletRequest request) {
		User user = new User();
		String username;
		String token = jwtAuthenticationFilter.getJwtFromRequest(request);		
		// Validate the token
		if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			// Get the User
			username = jwtTokenProvider.getUsernameFromJWT(token);
			user = userRepository.findByUsernameOrEmail(username, username)
					.orElseThrow(() -> new ResourceNotFoundException("User", "username or Email", 1L));
			return user;
		}
		throw new ShoppingCartException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid token")  ;
	}
	
	@Override
	public ShoppingCart addToShoppingCart(ShoppingCartDTO shoppingCartDTO, HttpServletRequest request) {
		User user = getTheUserFromRequest(request);
		// validate if the product id is valid
		Product product = productService.getById(shoppingCartDTO.getProductId());
		
		Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUser(user);
		if(shoppingCartOptional.isPresent()) {
			// the shopping cart already exists
			ShoppingCart shoppingCart = shoppingCartOptional.get();
			Set<CartItem> cartItems = shoppingCart.getCartItems();
			for(CartItem cartItem : cartItems) {
				if(cartItem.getProduct().getId() == product.getId()) {
					// both shopping cart and product already exist
					cartItem.setQuantity(cartItem.getQuantity() + 1);
					CartItem newCartItem = cartRepository.save(cartItem);
					//shoppingCart.addItem(newCartItem);
					return shoppingCartRepository.save(shoppingCart);
					
				}
			}
			// the shopping cart already exists but the cartItem doesn't
			CartItem cartItem = new CartItem();
			
			cartItem.setCreatedDate(new Date());
			cartItem.setProduct(product);
			cartItem.setQuantity(1);
			cartItem.setShoppingCart(shoppingCart);
			CartItem newCartItem = cartRepository.save(cartItem);
			shoppingCart.addItem(newCartItem);
			return shoppingCartRepository.save(shoppingCart);
			
		} else {
			// the shopping cart didn´t exist, the cartItem didn't exist either 
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCartRepository.save(shoppingCart);
			CartItem cartItem = new CartItem();
			
			cartItem.setCreatedDate(new Date());
			cartItem.setProduct(product);
			cartItem.setQuantity(1);
			cartItem.setShoppingCart(shoppingCart);
			
			
			shoppingCart.setAddress(user.getDefaultAddress());
			shoppingCart.addItem(cartItem);
			shoppingCart.setUser(user);
			CartItem newCartItem = cartRepository.save(cartItem);
			shoppingCart.addItem(newCartItem);
			return shoppingCartRepository.save(shoppingCart);		
		}
	}

	@Override
	public ShoppingCartListItemsDTO listCartItems(HttpServletRequest request) {
		User user = getTheUserFromRequest(request);
		Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUser(user);
		if(!shoppingCartOptional.isPresent()) {
			throw new ResourceNotFoundException("Shopping Cart List", "user_id", user.getId());
		}
		ShoppingCart shoppingCart = shoppingCartOptional.get();
		Set<CartItem> cartList = shoppingCart.getCartItems();
		List<CartItemDTO> cartItems = new ArrayList<>();
		double totalCost = 0;
		
		for(CartItem cart : cartList) {
			CartItemDTO cartItemDTO = new CartItemDTO(cart);
			totalCost += cartItemDTO.getQuantity() * cart.getProduct().getPrice();
			cartItems.add(cartItemDTO);
		}
		
		ShoppingCartListItemsDTO shoppingCartListItemsDTO = new ShoppingCartListItemsDTO();
		shoppingCartListItemsDTO.setTotalCost(totalCost);
		shoppingCartListItemsDTO.setCartItems(cartItems);
		shoppingCartListItemsDTO.setDeliveryAddress(user.getDefaultAddress());
		return shoppingCartListItemsDTO;
	}
	
	@Override
	public void reduceOrDeleteCartItem(Long cartItemId, HttpServletRequest request) {
		User user = getTheUserFromRequest(request);
		
		CartItem cart = cartRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart item", "cartItemId", cartItemId));
		
		ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Shopping Cart List", "user_id", user.getId()));
		
		if(!shoppingCart.getCartItems().contains(cart)) {
			throw new CartItemDoesNotBelongToUserException("Item does not belong to this user");
		}
		
		if(cart.getQuantity() == 1) {
			cartRepository.delete(cart);
		} else {
			cart.setQuantity(cart.getQuantity() - 1);
			cartRepository.save(cart);
		}
	}

	
	
}
