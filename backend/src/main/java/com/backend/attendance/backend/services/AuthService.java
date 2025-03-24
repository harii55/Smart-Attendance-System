package com.backend.attendance.backend.services;


import com.backend.attendance.backend.models.AuthRequest;
import com.backend.attendance.backend.models.AuthResponse;
import com.backend.attendance.backend.models.GoogleAuthRequest;
import com.backend.attendance.backend.models.User;
import com.backend.attendance.backend.repositories.StudentRepository;
import com.backend.attendance.backend.repositories.UserRepository;
import com.backend.attendance.backend.utils.EmailValidator;
import com.backend.attendance.backend.utils.JwtUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;



@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StudentRepository studentRepository;


    public ResponseEntity<?> loginOrRegister(@RequestBody AuthRequest authRequest) throws Exception {

        String email = authRequest.getEmail();
        if (studentRepository.lookforEmail(email)) {

            String password = authRequest.getPassword();
            Optional<String> rollNumber = EmailValidator.extractRollNumber(email);
            if (rollNumber.isEmpty()) {
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

        return ResponseEntity.badRequest().body("Email not registered");
    }


        public ResponseEntity<?> googleLogIn(@RequestBody GoogleAuthRequest googleAuthRequest) throws Exception {

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
