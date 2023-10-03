package com.yb.blogApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class ApiController {
	public final Logger log = LoggerFactory.getLogger(ApiController.class);
	
	protected static final String API_PATH = "/api/yB";
	
	// save Api SignUp
	protected static final  String  SIGNUP_USER = API_PATH +  "/signup";
	protected static final  String  SIGNUP_USER_GETALL_DETAILS = SIGNUP_USER +  "/getAll";
	protected static final  String  SIGNUP_USER_GETALL_ = SIGNUP_USER +  "/verify-Otp";
	
}
