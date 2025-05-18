package com.backend.attendance.backend.controllers;

import com.backend.attendance.backend.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance/token")
public class TokenController {

    @Autowired
    private JwtService jwtService;

    @Value("${app.id}")
    private String expectedAppId;

    @PostMapping
    public ResponseEntity<?> issueToken(
            @RequestHeader("X-APP-ID") String appId,
            @RequestParam("email") String email)
    {
        if (!expectedAppId.equals(appId)) {
            return ResponseEntity.status(403).body("Unauthorised Client");
        }

        String token = jwtService.generateToken(email);
        return ResponseEntity.ok(token);
    }
}