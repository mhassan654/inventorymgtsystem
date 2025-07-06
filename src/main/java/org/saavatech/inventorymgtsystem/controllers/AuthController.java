package org.saavatech.inventorymgtsystem.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.saavatech.inventorymgtsystem.dtos.LoginRequest;
import org.saavatech.inventorymgtsystem.dtos.RegisterRequest;
import org.saavatech.inventorymgtsystem.dtos.Response;
import org.saavatech.inventorymgtsystem.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

}
