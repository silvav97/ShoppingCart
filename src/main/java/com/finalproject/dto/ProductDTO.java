package com.finalproject.dto;

import javax.validation.constraints.NotNull;

public class ProductDTO {

	// for create it can be optional
	// for update we need the id;
	private Long id;
	private @NotNull String name;
	private @NotNull double price;
	private @NotNull String description;
	private @NotNull Long categoryId;

	public ProductDTO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

