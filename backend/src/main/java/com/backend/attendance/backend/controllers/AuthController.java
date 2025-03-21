package com.backend.attendance.backend.controllers;

import com.backend.attendance.backend.models.AuthRequest;
import com.backend.attendance.backend.models.AuthResponse;
import com.backend.attendance.backend.models.GoogleAuthRequest;
import com.backend.attendance.backend.models.User;
import com.backend.attendance.backend.repositories.UserRepository;
import com.backend.attendance.backend.services.AuthService;
import com.backend.attendance.backend.utils.EmailValidator;
import com.backend.attendance.backend.utils.JwtUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
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
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> loginOrRegister(@RequestBody AuthRequest authRequest) throws Exception {
        return ResponseEntity.ok(authService.loginOrRegister(authRequest));
    }


    @PostMapping("/google")
            public ResponseEntity<?> googleLogin(@RequestBody GoogleAuthRequest googleAuthRequest) throws Exception {
                return ResponseEntity.ok(authService.googleLogIn(googleAuthRequest));
            }

        }