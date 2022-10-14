package com.finalproject.service;

import java.util.List;

import com.finalproject.dto.CategoryDTO;

public interface CategoryService {
	
	public CategoryDTO createCategory(CategoryDTO categoryDTO);

	public List<CategoryDTO> getAllTheCategories();
	
	public CategoryDTO getCategoryById(Long id);
	
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id);
	
	public void deleteCategory(Long id);
	
}
