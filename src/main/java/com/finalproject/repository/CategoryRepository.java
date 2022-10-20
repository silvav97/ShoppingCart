package com.finalproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	public Optional<Category> findByCategoryName(String categoryName);

}
