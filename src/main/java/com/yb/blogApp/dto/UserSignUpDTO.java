package com.yb.blogApp.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserSignUpDTO {

	private String name;

	private String email;

	private String password;

	private String phoneNumber;

	private String dob;

	private String image;

	private String otp;
	private Date createdAt;
}
