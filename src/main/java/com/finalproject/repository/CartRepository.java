package com.finalproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.entity.CartItem;
import com.finalproject.entity.User;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

	public List<CartItem> findAllByUserOrderByCreatedDateDesc(User user);
}
