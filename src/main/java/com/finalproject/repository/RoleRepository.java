package com.finalproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	public Optional<Role> findByName(String name);
	
}
