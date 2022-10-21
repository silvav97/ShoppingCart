package com.finalproject.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.dto.CategoryDTO;
import com.finalproject.dto.ProductDTO;
import com.finalproject.security.CustomUserDetailsService;
import com.finalproject.security.JwtAuthenticationEntryPoint;
import com.finalproject.security.JwtTokenProvider;
import com.finalproject.service.CategoryServiceImpl;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;


@WebMvcTest(CategoryController.class)
public class CategoryControllerTests {
	
	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	
	@MockBean
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@MockBean
	private JwtTokenProvider jwtTokenProvider;
	
	@MockBean
	private CategoryServiceImpl categoryService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldCreateMockMvc() {
		assertNotNull(mockMvc);
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void GIVEN_THEN_Get_All_Categories() throws Exception {
		// given
		List<CategoryDTO> listCategoryDTO = new ArrayList<>();
		CategoryDTO categoryDTO = new CategoryDTO();		
		categoryDTO.setCategoryName("category name");
		categoryDTO.setDescription("description");
		categoryDTO.setId(1L);
		listCategoryDTO.add(categoryDTO);

		// when
		when(categoryService.getAllTheCategories()).thenReturn(listCategoryDTO);
		
		// then
		this.mockMvc.perform(get("http://localhost:8087/api/category")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].id").value(1L))
			.andExpect(jsonPath("$.[0].categoryName").value("category name"))
			.andExpect(jsonPath("$.[0].description").value("description"));
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void GIVEN_Category_Id_THEN_Get_Category() throws Exception {
		// given
		CategoryDTO categoryDTO = new CategoryDTO();		
		categoryDTO.setCategoryName("category name");
		categoryDTO.setDescription("description");
		categoryDTO.setId(1L);

		// when
		when(categoryService.getCategoryById(anyLong())).thenReturn(categoryDTO);
		
		// then
		this.mockMvc.perform(get("http://localhost:8087/api/category/1")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.categoryName").value("category name"))
			.andExpect(jsonPath("$.description").value("description"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"}, password = "adminPassword")
	void GIVEN_CategoryDTO_THEN_Create_Category() throws Exception {
		// given
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryName("category name");
		categoryDTO.setDescription("description");
		categoryDTO.setId(1L);
		
		// when
		when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(categoryDTO);
		
		// then
		this.mockMvc.perform(post("http://localhost:8087/api/category")
			.content(new ObjectMapper().writeValueAsString(categoryDTO))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.categoryName").value("category name"))
			.andExpect(jsonPath("$.description").value("description"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"}, password = "adminPassword")
	void GIVEN_CategoryDTO_And_Id_THEN_Update_Category() throws Exception {
		// given
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryName("category name");
		categoryDTO.setDescription("description");
		categoryDTO.setId(1L);
		
		// when
		when(categoryService.updateCategory(any(CategoryDTO.class), anyLong())).thenReturn(categoryDTO);
		
		// then
		this.mockMvc.perform(put("http://localhost:8087/api/category/1")
			.content(new ObjectMapper().writeValueAsString(categoryDTO))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.categoryName").value("category name"))
			.andExpect(jsonPath("$.description").value("description"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"}, password = "adminPassword")
	void GIVEN_Category_Id_THEN_Delete_Category() throws Exception {
		// given
		
		// when
		doNothing().when(categoryService).deleteCategory(anyLong());
		
		// then
		this.mockMvc.perform(delete("http://localhost:8087/api/category/1")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("Category deleted successfully"));
	}

}
