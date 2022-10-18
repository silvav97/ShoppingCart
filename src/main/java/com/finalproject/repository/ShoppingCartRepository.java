package com.finalproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;


@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

	public Optional<ShoppingCart> findByUser(User user);
}
