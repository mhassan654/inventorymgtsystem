package org.saavatech.inventorymgtsystem.dtos;

import jakarta.validation.constraints.NotBlank;
import org.saavatech.inventorymgtsystem.enums.UserRole;

public class RegisterRequest {
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    private UserRole role;
}
