package com.yb.blogApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login").permitAll() // Allow access to /login without authentication
                .antMatchers("/signup").permitAll() // Allow access to /signup without authentication
                .antMatchers("/api/yB**").authenticated() // Secure other API endpoints and require authentication

                // Configure access based on roles
                .antMatchers("/api/admin/**").hasRole("ADMIN") // Only users with ADMIN role can access /api/admin/**
                .antMatchers("/api/user/**").hasRole("USER")   // Only users with USER role can access /api/user/**
                .and()
            .httpBasic(); // Use Basic Authentication

        // Disable CSRF for simplicity (not recommended in production)
        http.csrf().disable();
    }
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


