package com.finalproject.service;

import java.util.List;
import java.util.stream.Collectors;

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

	// Map from Entity to DTO
	private ProductDTO mapToDTO(Product product) {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setPrice(product.getPrice());
		productDTO.setDescription(product.getDescription());
		productDTO.setCategoryName(product.getCategory().getCategoryName());
		return productDTO;
	}

	// Map from DTO to Entity
	private Product mapToEntity(ProductDTO productDTO) {
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		product.setDescription(productDTO.getDescription());
		return product;

	}

	@Override
	public ProductDTO createProduct(ProductDTO productDTO) {
		Category optionalCategory = categoryRepository.findByCategoryName(productDTO.getCategoryName())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id",
						categoryRepository.findByCategoryName(productDTO.getCategoryName()).get().getId()));

		Product product = mapToEntity(productDTO);
		product.setCategory(optionalCategory);
		Product newProduct = productRepository.save(product);
		return mapToDTO(newProduct);
	}

	@Override
	public List<ProductDTO> getAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());
	}
	
	@Override
	public ProductDTO getProductById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
		
		return mapToDTO(product);
	}
	
	@Override
	public ProductDTO updateProduct(ProductDTO productDTO, Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

		// Product product = mapToEntity(productDTO);
		// product.setCategory(optionalCategory);
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());

		Product productUpdated = productRepository.save(product);
		return mapToDTO(productUpdated);
	}


	@Override
	public void deleteProduct(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
		
		productRepository.delete(product);
	}

	@Override
	public Product getById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
		
		return product;
	}
	
}
