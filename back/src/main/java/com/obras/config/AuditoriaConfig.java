package com.obras.config;

import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

//config/AuditoriaConfig.java
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditoriaConfig {

 @Bean
 public AuditorAware<String> auditorProvider() {
     return () -> Optional.ofNullable(SecurityContextHolder.getContext())
         .map(SecurityContext::getAuthentication)
         .map(Authentication::getName)
         .or(() -> Optional.of("system"));
 }
}
