package com.yb.blogApp.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.alexaforbusiness.model.NotFoundException;
import com.yb.blogApp.dto.UserSignUpDTO;
import com.yb.blogApp.entity.UserSignUp;
import com.yb.blogApp.repo.UserSignUpServiceRepo;
import com.yb.blogApp.service.UserSignUpService;

@Service
public class UserServiceImpl implements UserSignUpService {

	@Autowired
	private UserSignUpServiceRepo userSignUpServiceRepo;

	@Autowired
	private PasswordEncoder passwordEncoder; // Autowire the PasswordEncoder

	@Override
	public void signup(UserSignUpDTO userSignUpDTO, String uniqueIdentifier, String otp) {
		UserSignUp existingUser = userSignUpServiceRepo.findByPhoneNumber(uniqueIdentifier);

		if (existingUser != null) {
			
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
			user.setRole(userSignUpDTO.getRole());
			userSignUpServiceRepo.save(user);
		}
	}

	@Override
	 public Optional<UserSignUp> findById(Long userId) {
	        return userSignUpServiceRepo.findById(userId);
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
	        user.setVerified(true); // Set verified to true

	        // Encode the password before saving
	        String encodedPassword = passwordEncoder.encode(user.getPassword());
	        user.setPassword(encodedPassword);

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

	@Override
	public UserSignUp findByEmail(String email) {

		return userSignUpServiceRepo.findByEmail(email);
	}

	


	@Override
    public void updateUserDetails(UserSignUpDTO userSignUpDTO, Long userId) {
        // Retrieve the user by user ID
        Optional<UserSignUp> optionalUser = userSignUpServiceRepo.findById(userId);

        if (optionalUser.isPresent()) {
            UserSignUp user = optionalUser.get();
            // Ensure that phone number and OTP remain unchanged
            String phoneNumber = user.getPhoneNumber();
            String otp = user.getOtp();

            // Update the user's details except phone number and OTP
            user.setName(userSignUpDTO.getName());
            user.setEmail(userSignUpDTO.getEmail());

            // Encode the password before updating
            String encodedPassword = passwordEncoder.encode(userSignUpDTO.getPassword());
            user.setPassword(encodedPassword);

            user.setDob(userSignUpDTO.getDob());
            user.setImage(userSignUpDTO.getImage());
            user.setCreatedAt(userSignUpDTO.getCreatedAt());

            user.setPhoneNumber(phoneNumber);
            user.setOtp(otp);

            userSignUpServiceRepo.save(user);
        } else {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
    }
	
	
}
