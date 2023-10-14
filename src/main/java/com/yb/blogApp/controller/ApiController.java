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
	protected static final  String  SIGNUP_USER_LOGIN = SIGNUP_USER +  "/login";
	protected static final  String SIGNUP_USER_GET_BY_ID = SIGNUP_USER + "/getById";
	protected static final  String SIGNUP_USER_UPDATE_BY_ID = SIGNUP_USER + "/updateById";
	
	//creating Blog of User
	protected static final  String  BlOG_ADD_USER = API_PATH + "/saveBlog";
	protected static final  String  BlOG_FIND_BY_ID = API_PATH + "/findBy/{id}";
	protected static final  String  BlOG_UPDATE_BY_ID = API_PATH + "/{id}";
	protected static final  String  DELETE_BY_ID = API_PATH + "/{id}";
	protected static final  String  BLOG_GET_ALL = API_PATH + "/blogGetALl";
	
}
