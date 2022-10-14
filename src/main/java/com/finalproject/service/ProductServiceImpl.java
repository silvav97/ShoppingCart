package com.finalproject.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.dto.ProductDTO;
import com.finalproject.entity.Category;
import com.finalproject.entity.Product;
import com.finalproject.exception.ResourceNotFoundException;
import com.finalproject.repository.CategoryRepository;
import com.finalproject.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	
	
	@Override
	public ProductDTO createProduct(ProductDTO productDTO) {
		
		
		Category optionalCategory = categoryRepository.findById(productDTO.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category","id",productDTO.getCategoryId()));
		
		//if(optionalCategory.getCategoryName().equals(category.getCategoryName())) { }
		Product product = mapToEntity(productDTO);
		product.setCategory(optionalCategory);
		
		//interface
		//Product product = new Product();
		//product.setName(productDTO.getName());
		//product.setDescription(productDTO.getDescription());
		//product.setPrice(productDTO.getPrice());
		//product.setCategory(category);
		Product newProduct = productRepository.save(product);
		
		return mapToDTO(newProduct);
	}
	
	// Map from Entity to DTO
	private ProductDTO mapToDTO(Product product) {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setPrice(product.getPrice());
		productDTO.setDescription(product.getDescription());
		productDTO.setCategoryId(product.getCategory().getId());
		
		return productDTO;
	}
	
	// Map from DTO to Entity
	private Product mapToEntity(ProductDTO productDTO) {
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		product.setDescription(productDTO.getDescription());
		//product.setCategoryId(productDTO.getCategory().getId());
		
		return product;
		
	}

}
