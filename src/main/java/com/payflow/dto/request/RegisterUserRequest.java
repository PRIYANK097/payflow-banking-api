package com.payflow.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterUserRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;


    @NotBlank(message = "Phone No. is required")
    @Pattern(regexp = "^[0-9]{10}$" , message = "Enter a valid 10 digit phone number")
    private String phone;
}
