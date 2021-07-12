package com.dataus.template.securityoauth2.config.audit;

import java.util.Optional;

import com.dataus.template.securityoauth2.member.principal.OAuth2UserImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
                OAuth2UserImpl principal = (OAuth2UserImpl) authentication.getPrincipal();

                return Optional.of(principal.getName());
            } catch (ClassCastException e) {} 

            return null;

        };
    }
    
}
