package com.finalproject.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.dto.ProductDTO;
import com.finalproject.dto.cartitem.OrderItemDTO;
import com.finalproject.dto.cartitem.OrderResponseDTO;
import com.finalproject.security.CustomUserDetailsService;
import com.finalproject.security.JwtAuthenticationEntryPoint;
import com.finalproject.security.JwtTokenProvider;
import com.finalproject.service.OrderServiceImpl;
import com.finalproject.service.ProductServiceImpl;
import com.finalproject.service.ShoppingCartServiceImpl;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import static org.mockito.ArgumentMatchers.anyLong;



@WebMvcTest(ProductController.class)
public class ProductControllerTests {

	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	
	@MockBean
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@MockBean
	private JwtTokenProvider jwtTokenProvider;
	
	@MockBean
	private ProductServiceImpl productService;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Test
	void shouldCreateMockMvc() {
		assertNotNull(mockMvc);
	}
	
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void GIVEN_THEN_Get_All_Products() throws Exception {
		// given
		List<ProductDTO> listProductDTO = new ArrayList<>();
		ProductDTO productDTO = new ProductDTO();
		productDTO.setCategoryName("category name");
		productDTO.setDescription("description");
		productDTO.setId(1L);
		productDTO.setName("name");
		productDTO.setPrice(10);
		listProductDTO.add(productDTO);
		
		// when
		when(productService.getAllProducts()).thenReturn(listProductDTO);
		
		// then
		this.mockMvc.perform(get("http://localhost:8087/api/product")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].id").value(1L))
			.andExpect(jsonPath("$.[0].categoryName").value("category name"))
			.andExpect(jsonPath("$.[0].name").value("name"))
			.andExpect(jsonPath("$.[0].description").value("description"));
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void GIVEN_Product_Id_THEN_Get_Product() throws Exception {
		// given
		ProductDTO productDTO = new ProductDTO();
		productDTO.setCategoryName("category name");
		productDTO.setDescription("description");
		productDTO.setId(1L);
		productDTO.setName("name");
		productDTO.setPrice(10);
		
		// when
		when(productService.getProductById(anyLong())).thenReturn(productDTO);
		
		// then
		this.mockMvc.perform(get("http://localhost:8087/api/product/1")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.categoryName").value("category name"))
			.andExpect(jsonPath("$.name").value("name"))
			.andExpect(jsonPath("$.description").value("description"));
	}
	
	@Test
	@WithMockUser(roles = {"ADMIN"})
	void GIVEN_ProductDTO_THEN_Create_Product() throws Exception {
		// given
		ProductDTO productDTO = new ProductDTO();
		productDTO.setCategoryName("category name");
		productDTO.setDescription("description");
		productDTO.setId(1L);
		productDTO.setName("name");
		productDTO.setPrice(10);
		
		
		// when
		when(productService.createProduct(any(ProductDTO.class))).thenReturn(productDTO);
		
		// then
		this.mockMvc.perform(post("http://localhost:8087/api/product")
			.content(new ObjectMapper().writeValueAsString(productDTO))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.categoryName").value("category name"))
			.andExpect(jsonPath("$.name").value("name"))
			.andExpect(jsonPath("$.description").value("description"));
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"ADMIN"}, password = "sebastianPassword")
	void GIVEN_ProductDTO_And_Id_THEN_Update_Product() throws Exception {
		// given
		ProductDTO productDTO = new ProductDTO();
		productDTO.setCategoryName("category name");
		productDTO.setDescription("description");
		productDTO.setId(1L);
		productDTO.setName("name");
		productDTO.setPrice(10);
		
		
		// when
		when(productService.updateProduct(any(ProductDTO.class), anyLong())).thenReturn(productDTO);
		
		// then
		this.mockMvc.perform(put("http://localhost:8087/api/product/1")
			.content(new ObjectMapper().writeValueAsString(productDTO))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.categoryName").value("category name"))
			.andExpect(jsonPath("$.name").value("name"))
			.andExpect(jsonPath("$.description").value("description"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"}, password = "adminPassword")
	void GIVEN_Product_Id_THEN_Delete_Product() throws Exception {
		// given
		
		// when
		doNothing().when(productService).deleteProduct(anyLong());
		
		// then
		this.mockMvc.perform(delete("http://localhost:8087/api/product/1")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("Product deleted successfully"));
	}
}
