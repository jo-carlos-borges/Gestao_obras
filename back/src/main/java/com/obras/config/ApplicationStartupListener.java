package com.obras.config;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.obras.domain.User;
import com.obras.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationStartupListener {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public ApplicationStartupListener(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (userRepository.count() == 0) {
			log.info("Criando usuário do sistema");
			User adminUser = User.builder().name("admin").password(passwordEncoder.encode("123")).build();
			userRepository.save(adminUser);
		}
	}
}