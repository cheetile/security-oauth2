package com.dataus.template.securityoauth2.config.audit;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
            
            if(authentication == null || !authentication.isAuthenticated()) {
                return null;
            }

            try {
                UserDetails principal = (UserDetails) authentication.getPrincipal();

                return Optional.of(principal.getUsername());
            } catch (ClassCastException e) {} 

            return null;

        };
    }
    
}
