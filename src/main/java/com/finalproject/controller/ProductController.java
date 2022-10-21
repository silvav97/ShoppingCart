package com.finalproject.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalproject.common.ApiResponse;
import com.finalproject.dto.ProductDTO;
import com.finalproject.service.ProductServiceImpl;

@RestController
@RequestMapping("/api/product")
public class ProductController {
	
	@Autowired
	private ProductServiceImpl productService;
	
	@GetMapping
	public ResponseEntity<List<ProductDTO>> getProducts() {
		return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
		return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductDTO productDTO) {
		return new ResponseEntity<>(productService.updateProduct(productDTO, id), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") Long id) {
		productService.deleteProduct(id);
		return new ResponseEntity<>(new ApiResponse(true,"Product deleted successfully"), HttpStatus.OK);
	}
}
