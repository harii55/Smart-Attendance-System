package com.backend.attendance.backend.controllers;

import com.backend.attendance.backend.models.AuthRequest;
import com.backend.attendance.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> loginOrRegister(@RequestBody AuthRequest authRequest) throws Exception {
        return authService.loginOrRegister(authRequest);
    }
}