package com.yb.blogApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.yb.blogApp.dto.UserSignUpDTO;
import com.yb.blogApp.entity.UserSignUp;


public interface UserSignUpService {
	

	void signup(UserSignUpDTO userSignUpDTO, String uniqueIdentifier, String otp);

	List<UserSignUp> findAllUsers();

	boolean verifyAndDeleteUnverifiedUsers(String phoneNumber, String otp);

	ResponseEntity<String> login(String email, String password);

	UserSignUp findByEmail(String email);
	
	
    void updateUserDetails(UserSignUpDTO userSignUpDTO, Long userId);

	Optional<UserSignUp> findById(Long userId);




}
