package com.yb.blogApp.response;

public class SignupResponse {
    private String message;
    private String otp;

    // Default constructor
    public SignupResponse() {
    }

    // Constructor with message and otp
    public SignupResponse(String message, String otp) {
        this.message = message;
        this.otp = otp;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for otp
    public String getOtp() {
        return otp;
    }

    // Setter for otp
    public void setOtp(String otp) {
        this.otp = otp;
    }
}
