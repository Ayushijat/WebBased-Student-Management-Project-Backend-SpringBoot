package com.example.main.controllers;

import com.example.main.dto.ApiResponse;
import com.example.main.dto.AuthResponse;
import com.example.main.dto.LoginRequest;
import com.example.main.dto.SignUpRequest;
import com.example.main.entities.Student;
import com.example.main.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private StudentService service;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignUpRequest request){
        String message = service.signup(request);
        ApiResponse response = new ApiResponse(
                true,
                message,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request){
        AuthResponse authResponse = service.login(request);
        ApiResponse response = new ApiResponse(
                true,
                "Login Successfully",
                authResponse
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(){
        String message = service.logout();
        ApiResponse response = new ApiResponse(
                true,
                message,
                null
        );
        return ResponseEntity.ok(response);
    }

}
