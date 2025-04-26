package com.obras.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.obras.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// Evita que aconteça de procurar usuario pelo name "ADMIN" e retornar usuario com name "admin".
	@Query(value = "SELECT * FROM User u WHERE BINARY u.name = :name", nativeQuery = true)
	public Optional<User> findByName(String name);
	
	public Boolean existsByName(String name);
	
}