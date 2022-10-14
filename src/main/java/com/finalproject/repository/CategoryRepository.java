package com.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finalproject.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
