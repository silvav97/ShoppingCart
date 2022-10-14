package com.finalproject.dto;

import javax.validation.constraints.NotBlank;

public class CategoryDTO {

	private Long id;
	private @NotBlank String categoryName;
	private @NotBlank String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CategoryDTO() {
		super();
	}

}
