package com.finalproject.service;

import java.util.List;

import com.finalproject.dto.ProductDTO;
import com.finalproject.entity.Category;

public interface ProductService {
	
	public ProductDTO createProduct(ProductDTO productDTO);

	public List<ProductDTO> getAllProducts();
	
	public ProductDTO getProductById(Long id);
	
	public ProductDTO updateProduct(ProductDTO productDTO, Long id);
	
	public void deleteProduct(Long id);
	
	
	
}
