package com.finalproject.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finalproject.dto.CategoryDTO;
import com.finalproject.entity.Category;
import com.finalproject.exception.ResourceNotFoundException;
import com.finalproject.repository.CategoryRepository;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTests {
	

	@Mock
	private CategoryRepository categoryRepository;
	
	@InjectMocks
	private CategoryServiceImpl categoryService;
	
	@Test
	void shouldWork() {
		assertEquals(0, 0);
	}
	
	@Test
	public void GIVEN_THEN_Create_Category() {
		// given
		Category category = new Category();
		CategoryDTO categoryDTO = new CategoryDTO();
		
		// when
		when(categoryRepository.save(any(Category.class))).thenReturn(category);
		
		// then
		CategoryDTO savedCategory = categoryService.createCategory(categoryDTO);
		assertThat(savedCategory).isNotNull();
	}
	

	@Test
	public void GIVEN_THEN_Get_List_Of_All_Categories() {
		// given
		Category category = new Category();
		List<Category> listCategory = new ArrayList<>();
		listCategory.add(category);
		
		// when
		when(categoryRepository.findAll()).thenReturn(listCategory);
		
		// then
		List<CategoryDTO> categories = categoryService.getAllTheCategories();
		assertThat(categories).isNotNull();
	}
	
	@Test
	public void GIVEN_CategoryId_THEN_Get_Category() {
		// given
		Category category = new Category();
		
		// when
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
		
		// then
		CategoryDTO obtainedCategory = categoryService.getCategoryById(anyLong());
		assertThat(obtainedCategory).isNotNull();
	}
	
	@Test
	public void GIVEN_Unexisting_CategoryId_THEN_Can_Not_Get_Category_And_Exception() {
		// given
		
		// when
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		// then
		try {
			categoryService.getCategoryById(anyLong());
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
		}
	}
	
	@Test
	public void GIVEN_CategoryId_THEN_Update_Category() {
		// given
		Category category = new Category();
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryName("category name");
		categoryDTO.setDescription("description");
		
		// when
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		// then
		CategoryDTO obtainedCategory = categoryService.updateCategory(categoryDTO, anyLong());
		assertThat(obtainedCategory).isNotNull();
	}
	
	@Test
	public void GIVEN_Unexisting_CategoryId_THEN_Can_Not_Update_And_Exception() {
		// given
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryName("category name");
		categoryDTO.setDescription("description");
		
		// when
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		// then
		try {
			categoryService.updateCategory(categoryDTO, anyLong());
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
		}
	}
	
	@Test
	public void GIVEN_CategoryId_THEN_Delete_Category() {
		// given
		Category category = new Category();
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryName("category name");
		categoryDTO.setDescription("description");
		
		// when
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
		doNothing().when(categoryRepository).delete(any(Category.class));

		// then
		categoryService.deleteCategory(anyLong());
	}
	
	@Test
	public void GIVEN_Unexisting_CategoryId_THEN_Can_Not_Delete_And_Exception() {
		// given
		
		// when
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		// then
		try {
			categoryService.deleteCategory(anyLong());;
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
		}
	}

}
