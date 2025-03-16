package com.backend.attendance.backend.controllers;

import com.backend.attendance.backend.models.AuthRequest;
import com.backend.attendance.backend.models.AuthResponse;
import com.backend.attendance.backend.models.GoogleAuthRequest;
import com.backend.attendance.backend.models.User;
import com.backend.attendance.backend.repositories.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> loginOrRegister(@RequestBody AuthRequest authRequest) throws Exception {
        String email = authRequest.getEmail();
        String password = authRequest.getPassword();

        Optional<String> rollNumber = EmailValidator.extractRollNumber(email);
        if (!rollNumber.isPresent()) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            // Login
            User user = userOptional.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid password");
            }

            String token = jwtUtil.generateToken(email);
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            // Register
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRollNumbeer(rollNumber.get());

            userRepository.save(newUser);

            String token = jwtUtil.generateToken(email);
            return ResponseEntity.ok(new AuthResponse(token));
        }
    }


    @PostMapping("/google")
            public ResponseEntity<?> googleLogin(@RequestBody GoogleAuthRequest googleAuthRequest) throws Exception {
                String idToken = googleAuthRequest.getIdToken();

                try {

                    FirebaseToken userRecord = FirebaseAuth.getInstance().verifyIdToken(idToken);
                    String email = userRecord.getEmail();

                    Optional<User> userOptional = userRepository.findByEmail(email);

                    if (userOptional.isEmpty()) {
 
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setPassword("");  // No password for Google login
                        newUser.setRollNumbeer(EmailValidator.extractRollNumber(email).orElse("")); // Extract roll number
                        userRepository.save(newUser);
                    }

                String token = jwtUtil.generateToken(email);

                    return ResponseEntity.ok(new AuthResponse(token));
                
                } catch (Exception e) {
                
                    return ResponseEntity.badRequest().body("Invalid Google ID token");
                
                }
            }

        }