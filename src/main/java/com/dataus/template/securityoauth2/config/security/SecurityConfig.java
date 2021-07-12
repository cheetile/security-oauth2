package com.dataus.template.securityoauth2.config.security;

import com.dataus.template.securityoauth2.common.property.AppProperties;
import com.dataus.template.securityoauth2.config.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.dataus.template.securityoauth2.config.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.dataus.template.securityoauth2.config.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.dataus.template.securityoauth2.member.service.CustomOAuth2UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public AuthTokenFilter authJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
                .and()
            .csrf()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthEntryPoint())
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .formLogin()
                .disable()
            .httpBasic()
                .disable()
            .oauth2Login()
                .authorizationEndpoint()
                    .baseUri("/oauth2/authorize")
                    .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                    .and()
                .redirectionEndpoint()
                    .baseUri("/oauth2/callback/*")
                    .and()
                .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                    .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
            .authorizeRequests()
                .antMatchers("/")
                    .permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/members/**")
                    .permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/members/**")
                    .permitAll()
                .anyRequest()
                    .authenticated();
        
        http.addFilterBefore(
            authJwtTokenFilter(), 
            UsernamePasswordAuthenticationFilter.class);
                
    }
    
}
