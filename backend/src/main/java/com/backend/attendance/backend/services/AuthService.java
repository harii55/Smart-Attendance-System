package com.backend.attendance.backend.services;


import com.backend.attendance.backend.models.AuthRequest;
import com.backend.attendance.backend.models.AuthResponse;
import com.backend.attendance.backend.models.User;
import com.backend.attendance.backend.repositories.StudentRepository;
import com.backend.attendance.backend.repositories.UserRepository;
import com.backend.attendance.backend.utils.EmailValidator;
import com.backend.attendance.backend.utils.JwtUtil;
import com.backend.attendance.backend.utils.StudentProvider;
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

    @Autowired
    private StudentProvider studentProvider;


    public ResponseEntity<?> loginOrRegister(@RequestBody AuthRequest authRequest) throws Exception {

        String email = authRequest.getEmail();
        Optional<String> rollNumber = EmailValidator.extractRollNumber(email);
        String password = authRequest.getPassword();

        if (rollNumber.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        if (!EmailValidator.isValidEmail(email)) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        if (studentProvider.getStudentDirectory().containsKey(email)) {

            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {

                User user = userOptional.get();
                if (!passwordEncoder.matches(password, user.getPassword())) {
                    return ResponseEntity.badRequest().body("Invalid password");
                }

                String token = jwtUtil.generateToken(email);
                return ResponseEntity.ok(new AuthResponse(token));
            }else {

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
}
