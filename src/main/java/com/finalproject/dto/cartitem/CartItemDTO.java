package com.finalproject.dto.cartitem;

import com.finalproject.dto.ProductDTO;
import com.finalproject.entity.CartItem;

public class CartItemDTO {

	private Long id;
	private Integer quantity;
	private ProductDTO productDTO;

	public CartItemDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}
	
	
	public CartItemDTO(CartItem cart) {
		this.id = cart.getId();
		this.quantity = cart.getQuantity();
		this.productDTO = new ProductDTO(cart.getProduct().getId(),
										cart.getProduct().getName(),
										cart.getProduct().getPrice(),
										cart.getProduct().getDescription(),
										cart.getProduct().getCategory().getCategoryName());
	}
	

}
