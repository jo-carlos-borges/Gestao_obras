package com.obras.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obras.records.UserRecordInput;
import com.obras.security.JwtHelper;

import jakarta.validation.Valid;

@RestController()
@RequestMapping("/api/auth")
public class LoginController {
	
	private final AuthenticationManager authenticationManager;
	private final JwtHelper jwtHelper;
	
	public LoginController(AuthenticationManager authenticationManager, JwtHelper jwtHelper) {
		this.authenticationManager = authenticationManager;
		this.jwtHelper = jwtHelper;
	}
	
	@PostMapping
	public ResponseEntity<Object> login(@Valid @RequestBody UserRecordInput record) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(record.name(), record.password()));
	    String token = jwtHelper.generateToken(record.name());
	    return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	
}