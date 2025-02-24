package com.backend.attendance.backend.controllers;

import com.backend.attendance.backend.models.AuthRequest;
import com.backend.attendance.backend.models.AuthResponse;
import com.backend.attendance.backend.models.User;
import com.backend.attendance.backend.repositories.UserRepository;
import com.backend.attendance.backend.utils.EmailValidator;
import com.backend.attendance.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/attendance/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> loginOrRegister(@RequestBody AuthRequest authRequest) throws Exception {
        // TODO: Write a function to handle the login or register...
        return ResponseEntity.ok("login");
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody AuthRequest authRequest) throws Exception {
        // TODO: Write a function to handle google login...
        return ResponseEntity.ok("google");
    }
}
