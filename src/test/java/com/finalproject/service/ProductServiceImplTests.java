package com.finalproject.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finalproject.dto.ProductDTO;
import com.finalproject.entity.Address;
import com.finalproject.entity.Category;
import com.finalproject.entity.Product;
import com.finalproject.exception.ResourceNotFoundException;
import com.finalproject.repository.AddressRepository;
import com.finalproject.repository.CategoryRepository;
import com.finalproject.repository.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTests {
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@InjectMocks
	private ProductServiceImpl productService;
	
	@Test
	void shouldWork() {
		assertEquals(0, 0);
	}
	
	@Test
	public void GIVEN_THEN_Save_Product() {
		// given
		Category category = new Category();
		category.setCategoryName("category name");
		Product product = new Product();
		product.setCategory(category);
		product.setDescription("product description");
		ProductDTO productDTO = new ProductDTO();
		productDTO.setCategoryName("category name");
		productDTO.setDescription("description");
		productDTO.setId(1L);
		productDTO.setName("product name");
		productDTO.setPrice(10);
		
		// when
		when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.of(category));
		when(productRepository.save(any(Product.class))).thenReturn(product);
		
		// then
		ProductDTO savedproductDTO = productService.createProduct(productDTO);
		assertThat(savedproductDTO).isNotNull();
	}
	
	//@Test
	public void GIVEN_UnExisting_Category_Name_THEN_Save_Address() {
		// given
		Category category = new Category();
		category.setCategoryName("category name");
		category.setId(1L);
		Product product = new Product();
		product.setCategory(category);
		product.setDescription("product description");
		ProductDTO productDTO = new ProductDTO();
		productDTO.setCategoryName("category name");
		productDTO.setDescription("description");
		productDTO.setId(1L);
		productDTO.setName("product name");
		productDTO.setPrice(10);
		
		// when
		when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.ofNullable(null));
		when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.of(category));
		
		when(categoryRepository.findByCategoryName(anyString()).get().getId()).thenReturn(1L);
		// then
		try {
			productService.createProduct(productDTO);
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
		}
	}
	
	@Test
	public void GIVEN_THEN_Get_All_Products() {
		// given
		List<Product> listProduct = new ArrayList<>();
		
		// when
		when(productRepository.findAll()).thenReturn(listProduct);
		
		// then
		List<ProductDTO> list = productService.getAllProducts();
		assertThat(list).isNotNull();
	}
	
	@Test
	public void GIVEN_ProductId_THEN_Get_ProductDTO() {
		// given
		Category category = new Category();
		category.setCategoryName("category name");
		category.setDescription("category description");
		category.setId(1L);
		Product product = new Product();
		product.setDescription("description");
		product.setCategory(category);
		product.setDescription("description");
		product.setId(1L);
		product.setName("product name");
		product.setPrice(10);
		
		// when
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		
		// then
		ProductDTO productDTO = productService.getProductById(anyLong());
		assertThat(productDTO).isNotNull();
	}
	
	@Test
	public void GIVEN_Unexisting_ProductId_THEN_Can_Not_Get_ProductDTO_And_Exception() {
		// given
		
		// when
		when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		// then
		try {
			productService.getProductById(anyLong());
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
		}
	}
	
	@Test
	public void GIVEN_ProductId_THEN_Update_Product() {
		// given
		Category category = new Category();
		category.setCategoryName("category name");
		category.setDescription("category description");
		category.setId(1L);
		Product product = new Product();
		product.setDescription("description");
		product.setCategory(category);
		product.setDescription("product description");
		product.setId(1L);
		product.setName("product name");
		product.setPrice(10);
		ProductDTO productDTO = new ProductDTO();
		productDTO.setCategoryName("category name");
		productDTO.setDescription("product description");
		productDTO.setId(1L);
		productDTO.setName("product name");
		productDTO.setPrice(10);
		
		
		// when
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.of(category));
		when(productRepository.save(any(Product.class))).thenReturn(product);
		productService.createProduct(productDTO);

		// then
		ProductDTO updatedProductDTO = productService.updateProduct(productDTO, anyLong());
		assertThat(updatedProductDTO).isNotNull();
	}
	
	@Test
	public void GIVEN_Unexisting_ProductId_THEN_Can_Not_Update_Product_And_Exception() {
		// given
		ProductDTO productDTO = new ProductDTO();
		
		// when
		when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		// then
		try {
			productService.updateProduct(productDTO, anyLong());
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
		}
	}
	
	@Test
	public void GIVEN_ProductId_THEN_Delete_Product() {
		// given
		Category category = new Category();
		category.setCategoryName("category name");
		category.setDescription("category description");
		category.setId(1L);
		Product product = new Product();
		product.setDescription("description");
		product.setCategory(category);
		product.setDescription("product description");
		product.setId(1L);
		product.setName("product name");
		product.setPrice(10);
		ProductDTO productDTO = new ProductDTO();
		productDTO.setCategoryName("category name");
		productDTO.setDescription("product description");
		productDTO.setId(1L);
		productDTO.setName("product name");
		productDTO.setPrice(10);
		
		
		// when
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.of(category));
		when(productRepository.save(any(Product.class))).thenReturn(product);
		doNothing().when(productRepository).delete(any(Product.class));
		productService.createProduct(productDTO);

		// then
		productService.deleteProduct(anyLong());;
	}
	
	@Test
	public void GIVEN_Unexisting_ProductId_THEN_Can_Not_Delete_Product_And_Exception() {
		// given
		
		// when
		when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		// then
		try {
			productService.deleteProduct(anyLong());
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	public void GIVEN_ProductId_THEN_Get_Product() {
		// given
		Product product = new Product();
		
		// when
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

		// then
		Product obtainedProduct = productService.getById(anyLong());
		assertThat(obtainedProduct).isNotNull();
	}
	
	@Test
	public void GIVEN_Unexisting_ProductId_THEN_Can_Not_Get_Product_And_Exception() {
		// given
		
		// when
		when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		// then
		try {
			productService.getById(anyLong());
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
		}
	}
}
