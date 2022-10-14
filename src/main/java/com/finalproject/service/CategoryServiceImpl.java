package com.finalproject.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.dto.CategoryDTO;
import com.finalproject.entity.Category;
import com.finalproject.exception.ResourceNotFoundException;
import com.finalproject.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	// Convert from Entity to DTO
	private CategoryDTO mapToDTO(Category category) {
		CategoryDTO categoryDTO = new CategoryDTO();

		categoryDTO.setId(category.getId());
		categoryDTO.setCategoryName(category.getCategoryName());
		categoryDTO.setDescription(category.getDescription());

		return categoryDTO;
	}

	// Convert from DTO to Entity
	private Category mapToEntity(CategoryDTO categoryDTO) {
		Category category = new Category();

		category.setCategoryName(categoryDTO.getCategoryName());
		category.setDescription(categoryDTO.getDescription());

		return category;
	}

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		Category category = mapToEntity(categoryDTO);
		Category newCategory = categoryRepository.save(category);
		CategoryDTO categoryResponse = mapToDTO(newCategory);
		return categoryResponse;
	}

	@Override
	public List<CategoryDTO> getAllTheCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream().map(category -> mapToDTO(category)).collect(Collectors.toList());
	}

	@Override
	public CategoryDTO getCategoryById(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
		return mapToDTO(category);
	}

	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
		
		category.setCategoryName(categoryDTO.getCategoryName());
		category.setDescription(categoryDTO.getDescription());
		Category categoryUpdated = categoryRepository.save(category);
		return mapToDTO(categoryUpdated);
	}

	@Override
	public void deleteCategory(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
		
		categoryRepository.delete(category);
	}

}
