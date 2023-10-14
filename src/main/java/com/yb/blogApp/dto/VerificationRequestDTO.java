package com.yb.blogApp.dto;

import lombok.Data;

@Data
public class VerificationRequestDTO {
    private String phoneNumber;
    private String otp;
}