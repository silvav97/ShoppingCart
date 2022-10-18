package com.finalproject.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "shoppingcart")
public class ShoppingCart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@OneToMany // (mappedBy = "shopping_cart", cascade = CascadeType.ALL, orphanRemoval = true)
	//@JoinTable(name = "shoppingcart_cartitems", joinColumns = @JoinColumn(name = "shopping_cart_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "cart_items_id", referencedColumnName = "id"))
	@OneToMany(mappedBy = "shoppingCart",  orphanRemoval = true) //cascade = CascadeType.ALL,
	@JsonBackReference
	private Set<CartItem> cartItems = new HashSet<>();
	
	//@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	//@JoinTable(name = "users_addresses", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "address_id", referencedColumnName = "id"))
	//private Set<Address> addresses = new HashSet<>();
	
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private String address;

	public ShoppingCart() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public CartItem addItem(CartItem cartItem) {
		this.cartItems.add(cartItem);
		return cartItem;
	}
	
	
	
}
