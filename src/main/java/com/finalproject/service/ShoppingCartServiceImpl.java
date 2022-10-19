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

import com.finalproject.dto.CreateAddressDTO;
import com.finalproject.dto.UpdateAddressDTO;
import com.finalproject.dto.UpdatePaymentMethodDTO;
import com.finalproject.dto.cartitem.CartItemDTO;
import com.finalproject.dto.cartitem.ShoppingCartDTO;
import com.finalproject.dto.cartitem.ShoppingCartListItemsDTO;
import com.finalproject.entity.Address;
import com.finalproject.entity.CartItem;
import com.finalproject.entity.PaymentMethod;
import com.finalproject.entity.Product;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;
import com.finalproject.exception.AddressDoesNotBelongToUserException;
import com.finalproject.exception.CartItemDoesNotBelongToUserException;
import com.finalproject.exception.ResourceNotFoundException;
import com.finalproject.exception.ShoppingCartException;
import com.finalproject.exception.UserDoesNotHaveAShoppingCartException;
import com.finalproject.repository.AddressRepository;
import com.finalproject.repository.CartRepository;
import com.finalproject.repository.PaymentMethodRepository;
import com.finalproject.repository.ShoppingCartRepository;
import com.finalproject.repository.UserRepository;
import com.finalproject.security.JwtAuthenticationFilter;
import com.finalproject.security.JwtTokenProvider;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private ProductServiceImpl productService;

	// CartRepository ?? -> Yes
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private PaymentMethodRepository paymentMethodRepository;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserRepository userRepository;
	
	
	// Method to get the user
	public User getTheUserFromRequest(HttpServletRequest request) {
		User user = new User();
		String username;
		String token = jwtAuthenticationFilter.getJwtFromRequest(request);
		// Validate the token
		if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			// Get the User
			username = jwtTokenProvider.getUsernameFromJWT(token);
			user = userRepository.findByUsernameOrEmail(username, username)
					.orElseThrow(() -> new ResourceNotFoundException("User", "username or Email", 1L));
			return user;
		}
		throw new ShoppingCartException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid token");
	}


	@Override
	public ShoppingCart addToShoppingCart(ShoppingCartDTO shoppingCartDTO, HttpServletRequest request) {
		User user = getTheUserFromRequest(request);
		// validate if the product id is valid
		Product product = productService.getById(shoppingCartDTO.getProductId());

		Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUser(user);
		if (shoppingCartOptional.isPresent()) {
			// the shopping cart already exists
			ShoppingCart shoppingCart = shoppingCartOptional.get();
			Set<CartItem> cartItems = shoppingCart.getCartItems();
			for (CartItem cartItem : cartItems) {
				if (cartItem.getProduct().getId() == product.getId()) {
					// both shopping cart and product already exist
					cartItem.setQuantity(cartItem.getQuantity() + 1);
					CartItem newCartItem = cartRepository.save(cartItem);
					// shoppingCart.addItem(newCartItem);
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

			shoppingCart.setAddress(user.getCurrentAddress());
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
		if (!shoppingCartOptional.isPresent()) {
			throw new ResourceNotFoundException("Shopping Cart List", "user_id", user.getId());
		}
		ShoppingCart shoppingCart = shoppingCartOptional.get();
		Set<CartItem> cartList = shoppingCart.getCartItems();
		List<CartItemDTO> cartItems = new ArrayList<>();
		double totalCost = 0;

		for (CartItem cart : cartList) {
			CartItemDTO cartItemDTO = new CartItemDTO(cart);
			totalCost += cartItemDTO.getQuantity() * cart.getProduct().getPrice();
			cartItems.add(cartItemDTO);
		}

		ShoppingCartListItemsDTO shoppingCartListItemsDTO = new ShoppingCartListItemsDTO();
		shoppingCartListItemsDTO.setTotalCost(totalCost);
		shoppingCartListItemsDTO.setCartItems(cartItems);
		shoppingCartListItemsDTO.setDeliveryAddress(user.getCurrentAddress());
		shoppingCartListItemsDTO.setUserName(user.getUsername());
		shoppingCartListItemsDTO.setUserEmail(user.getEmail());
		return shoppingCartListItemsDTO;
	}

	@Override
	public void reduceOrDeleteCartItem(Long cartItemId, HttpServletRequest request) {
		User user = getTheUserFromRequest(request);

		CartItem cart = cartRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart item", "cartItemId", cartItemId));

		ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
				.orElseThrow(() -> new UserDoesNotHaveAShoppingCartException("the user '"+user.getUsername() +"' does not have a shopping cart"));

		if (!shoppingCart.getCartItems().contains(cart)) {
			throw new CartItemDoesNotBelongToUserException("Item does not belong to this user");
		}

		if (cart.getQuantity() == 1) {
			for(CartItem cartItem: shoppingCart.getCartItems()) {
				if(cartItem.getId() == cart.getId())  {
					shoppingCart.getCartItems().remove(cartItem);
					break;
				}
			}
			cartRepository.delete(cart); 
			ShoppingCart shoppingCartUpdated = shoppingCartRepository.findByUser(user)
					.orElseThrow(() -> new UserDoesNotHaveAShoppingCartException("the user '"+user.getUsername() +"' does not have a shopping cart"));

			if(shoppingCartUpdated.getCartItems().isEmpty()) {
				shoppingCartRepository.delete(shoppingCartUpdated);
			}
		} 
		else {
			cart.setQuantity(cart.getQuantity() - 1);
			cartRepository.save(cart);
		}
	}

	@Override
	public void addAddress(CreateAddressDTO createAddressDTO, HttpServletRequest request) {
		User user = getTheUserFromRequest(request);
		Address newAddress = new Address();
		newAddress.setUserAddres(createAddressDTO.getNewAddress());
		user.addAddress(newAddress);
		userRepository.save(user);
	}

	@Override
	public User updateCurrentAddress(UpdateAddressDTO updateAddressDTO, HttpServletRequest request) {
		User user = getTheUserFromRequest(request);

		// check if that address exists
		Address updatedCurrentAddress = addressRepository.findById(updateAddressDTO.getCurrentAddressId())
				.orElseThrow(() -> new ResourceNotFoundException("Address", "Address_id", updateAddressDTO.getCurrentAddressId()));

		// check if that address belongs to user
		Set<Address> addresses = user.getAddresses();
		if (!addresses.contains(updatedCurrentAddress)) {
			throw new AddressDoesNotBelongToUserException("The address does not belong to user");
		}

		// check if the updateAddressDTOId belongs to user
		for (Address address : addresses) {
			if (address.getId() == updateAddressDTO.getCurrentAddressId()) {
				// the address belongs to the user
				user.setCurrentAddress(address.getUserAddres());
				
				// If user already has a shopping cart lets update their shoppingCart address
				Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUser(user);
				if(shoppingCartOptional.isPresent()) {
					ShoppingCart shoppingCart = shoppingCartOptional.get();
					shoppingCart.setAddress(user.getCurrentAddress());
					shoppingCartRepository.save(shoppingCart);
				}
				return userRepository.save(user);
			}
		}
		return user;
	}
	
	@Override
	public void updatePaymentMethod(UpdatePaymentMethodDTO updatePaymentMethodDTO, HttpServletRequest request) {
		User user = getTheUserFromRequest(request);
		PaymentMethod paymentMethod = paymentMethodRepository.findById(updatePaymentMethodDTO.getPaymentMethodId())
				.orElseThrow(() -> new ResourceNotFoundException("PaymentMethod", "PaymentMethod_id", updatePaymentMethodDTO.getPaymentMethodId()));

		user.setPaymentMethod(paymentMethod);
		userRepository.save(user);
	}

}
