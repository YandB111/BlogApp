package com.yb.blogApp.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yb.blogApp.dto.UserSignUpDTO;
import com.yb.blogApp.entity.UserSignUp;
import com.yb.blogApp.repo.UserSignUpServiceRepo;
import com.yb.blogApp.service.UserSignUpService;

@Service
public class UserServiceImpl implements UserSignUpService {

	@Autowired
	private UserSignUpServiceRepo userSignUpServiceRepo;

	
	@Override
	public void signup(UserSignUpDTO userSignUpDTO, String uniqueIdentifier, String otp) {
		UserSignUp existingUser = userSignUpServiceRepo.findByPhoneNumber(uniqueIdentifier);

		if (existingUser != null) {
			// Update the existing user's OTP
			existingUser.setOtp(otp);
			userSignUpServiceRepo.save(existingUser);
		} else {

			UserSignUp user = new UserSignUp();
			user.setName(userSignUpDTO.getName());
			user.setEmail(userSignUpDTO.getEmail());
			user.setPassword(userSignUpDTO.getPassword());
			user.setDob(userSignUpDTO.getDob());
			user.setPhoneNumber(userSignUpDTO.getPhoneNumber());
			user.setImage(userSignUpDTO.getImage());
			user.setCreatedAt(userSignUpDTO.getCreatedAt());
			user.setOtp(otp);

			userSignUpServiceRepo.save(user);
		}
	}

	@Override
	public List<UserSignUp> findAllUsers() {
		return userSignUpServiceRepo.findAll();
	}

	@Override
	public boolean verifyAndDeleteUnverifiedUsers(String phoneNumber, String otp) {
		// Find the user by phone number
		UserSignUp user = userSignUpServiceRepo.findByPhoneNumber(phoneNumber);

		if (user != null && user.getOtp() != null && user.getOtp().equals(otp)) {
			// OTP matches, mark the user as verified
			user.setVerified(true);
			userSignUpServiceRepo.save(user);
			return true; // OTP verification successful
		} else {
			// OTP doesn't match or user not found, delete the user after one day
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date oneDayAgo = calendar.getTime();

			List<UserSignUp> unverifiedUsers = userSignUpServiceRepo.findByVerifiedFalseAndCreatedAtBefore(oneDayAgo);

			userSignUpServiceRepo.deleteAll(unverifiedUsers);
			return false;
		}

	}

	@Override
	public ResponseEntity<String> login(String email, String password) {
		// Find the user by email
		UserSignUp user = userSignUpServiceRepo.findByEmail(email);

		if (user != null) {
			// Check if the user is verified
			if (user.isVerified()) {
				// Compare the provided password with the password from the database
				if (password.equals(user.getPassword())) {
					// Passwords match, user is authenticated
					return ResponseEntity.ok("Logged in successfully. Welcome, " + user.getName());
				}
			} else {
				// User is not verified, return a message indicating verification is required
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("Account not verified. Please verify your email.");
			}
		}

		// Authentication failed
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
	}

}
