package com.obras.security;

import java.util.NoSuchElementException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.obras.domain.User;
import com.obras.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String name){
    User user = userRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("User not found"));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getName())
        .password(user.getPassword())
        .build();
  }
}