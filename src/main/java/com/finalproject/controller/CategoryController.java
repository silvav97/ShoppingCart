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

import com.finalproject.dto.CategoryDTO;
import com.finalproject.service.CategoryServiceImpl;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
	
	@Autowired
	private CategoryServiceImpl categoryService;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> listCategories() {
		return new ResponseEntity<>(categoryService.getAllTheCategories(), HttpStatus.OK);

	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable(name="id") Long id) {
		return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryDTO> saveCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
		return new ResponseEntity<>(categoryService.createCategory(categoryDTO), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @PathVariable(name="id") Long id, @RequestBody CategoryDTO categoryDTO) {
		CategoryDTO categoryResponse = categoryService.updateCategory(categoryDTO, id);
		return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable(name="id") Long id) {
		categoryService.deleteCategory(id);
		return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
	}


}
