package com.yb.blogApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.amazonaws.services.alexaforbusiness.model.NotFoundException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.yb.blogApp.dto.LoginRequestDTO;
import com.yb.blogApp.dto.UserSignUpDTO;
import com.yb.blogApp.dto.VerificationRequestDTO;
import com.yb.blogApp.entity.UserSignUp;
import com.yb.blogApp.service.UserSignUpService;

import io.swagger.annotations.Api;

@RestController
@Api("SIGNUP AND LOGIN API")
public class UserLoginController extends ApiController {

	@Autowired
	UserSignUpService userSignUpService;

	@Value("${twilio.account-sid}")
	private String twilioAccountSid;

	@Value("${twilio.auth-token}")
	private String twilioAuthToken;

	@Value("${twilio.phone-number}")
	private String twilioPhoneNumber;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Signup successful. OTP sent to your phone."),
			@ApiResponse(code = 500, message = "Error during signup.") })
	@CrossOrigin
	@PostMapping(value = SIGNUP_USER, produces = "application/json")
	public ResponseEntity<String> signup(@RequestBody UserSignUpDTO userSignUpDTO) {
		try {
			String phoneNumber = userSignUpDTO.getPhoneNumber(); // Get the user's phone number

			Twilio.init(twilioAccountSid, twilioAuthToken);
			String otp = generateOtp();
			Message message = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(twilioPhoneNumber),
					"Yellow And Black OTP for signup is: " + otp).create();

			userSignUpService.signup(userSignUpDTO, phoneNumber, otp); // Pass the phone number and OTP

			return ResponseEntity.ok("Signup successful. OTP sent to your phone.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during signup.");
		}
	}

	private String generateOtp() {
		// Generate a random 6-digit OTP
		int min = 100000; // Minimum OTP value
		int max = 999999; // Maximum OTP value
		return String.valueOf((int) (Math.random() * (max - min + 1)) + min);
	}

	@CrossOrigin
	@GetMapping(value = SIGNUP_USER_GETALL_DETAILS, produces = "application/json")
	public ResponseEntity<List<UserSignUp>> getAllUsers() {
		List<UserSignUp> users = userSignUpService.findAllUsers();
		return ResponseEntity.ok(users);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = "OTP verification successful. Account is verified."),
			@ApiResponse(code = 400, message = "OTP verification failed. Account not verified.") })
	@CrossOrigin
	@PostMapping(value = SIGNUP_USER_GETALL_, produces = "application/json")
	public ResponseEntity<String> verifyOtp(@RequestBody VerificationRequestDTO request) {
		String phoneNumber = request.getPhoneNumber();
		String otp = request.getOtp();

		boolean isVerified = userSignUpService.verifyAndDeleteUnverifiedUsers(phoneNumber, otp);

		if (isVerified) {
			return ResponseEntity.ok("OTP verification successful. Account is verified.");
		} else {
			return ResponseEntity.badRequest().body("OTP verification failed. Account not verified.");
		}
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Login successful. Welcome {username}"),
			@ApiResponse(code = 401, message = "Authentication failed. Invalid password."),
			@ApiResponse(code = 401, message = "Authentication failed. User not found.") })
	@PostMapping(value = SIGNUP_USER_LOGIN, produces = "application/json")
	public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
		String email = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		UserSignUp user = userSignUpService.findByEmail(email);

		if (user != null) {
			if (passwordEncoder.matches(password, user.getPassword())) {
				return ResponseEntity.ok("Login successful. Welcome " + user.getName());
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed. Invalid password.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed. User not found.");
		}
	}

	@CrossOrigin
	@GetMapping(value = SIGNUP_USER_GET_BY_ID, produces = "application/json")
	public ResponseEntity<UserSignUp> getUserById(@PathVariable Long userId) {
		Optional<UserSignUp> userOptional = userSignUpService.findById(userId);

		if (userOptional.isPresent()) {
			UserSignUp user = userOptional.get();
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@CrossOrigin
	@PutMapping(value = SIGNUP_USER_UPDATE_BY_ID, produces = "application/json")
	public ResponseEntity<String> updateUserDetails(@PathVariable Long userId,
			@RequestBody UserSignUpDTO userSignUpDTO) {
		try {
			userSignUpService.updateUserDetails(userSignUpDTO, userId);
			return ResponseEntity.ok("User details updated successfully.");
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user details.");
		}
	}

	@CrossOrigin
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')") // Only users with role ADMIN can access this
	public ResponseEntity<String> adminEndpoint() {
		return ResponseEntity.ok("Welcome to the admin endpoint!");
	}

}
