package com.yb.blogApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.yb.blogApp.dto.LoginRequestDTO;
import com.yb.blogApp.dto.UserSignUpDTO;
import com.yb.blogApp.dto.VerificationRequestDTO;
import com.yb.blogApp.entity.UserSignUp;
import com.yb.blogApp.repo.UserSignUpServiceRepo;
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



	@CrossOrigin
	@PostMapping(value = SIGNUP_USER, produces = "application/json")
	public ResponseEntity<String> signup(@RequestBody UserSignUpDTO employeeDTO) {
		try {
			String phoneNumber = employeeDTO.getPhoneNumber(); // Get the user's phone number

			Twilio.init(twilioAccountSid, twilioAuthToken);
			String otp = generateOtp();
			Message message = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(twilioPhoneNumber),
					"Yellow And Black OTP for signup is: " + otp).create();

			userSignUpService.signup(employeeDTO, phoneNumber, otp); // Pass the phone number and OTP

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

	@CrossOrigin
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
	    String email = loginRequest.getEmail();
	    String password = loginRequest.getPassword();

	    // Call a service method to perform the login logic
	    ResponseEntity<String> response = userSignUpService.login(email, password);

	    return response;
	}

}
