package com.finalproject.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.finalproject.entity.CartItem;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

	//public List<CartItem> findAllByUserOrderByCreatedDateDesc(User user);
	//public boolean delete(Long cartItemId);
	
	//@Modifying
	//@Query("delete from cartitem t where t.id = ?1")
	//void delete(Long cartItemId);
}
