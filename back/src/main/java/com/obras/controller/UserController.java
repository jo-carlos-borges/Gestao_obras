package com.obras.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obras.records.UserRecordInput;
import com.obras.services.UserService;

import jakarta.validation.Valid;

@RestController()
@RequestMapping("/api/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("{id}")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		return userService.getById(id);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		return userService.delete(id);
	}

	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid UserRecordInput record) {
		return userService.update(id, record);
	}
	
	@GetMapping
	public ResponseEntity<Object> get(Pageable pageable) {
		return userService.get(pageable);
	}
	
	@PostMapping
	public ResponseEntity<Object> save(@RequestBody @Valid UserRecordInput record) {
		return userService.save(record);
	}
}