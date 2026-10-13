package com.restaurant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminAuthController {

    @Value("${admin.password}")
    private String adminPassword;

    private final AdminTokenService tokenService;

    public AdminAuthController(AdminTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request) {

        String password = request.get("password");

        if (password == null ||
                !adminPassword.equals(password)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid password"));
        }

        String token = tokenService.createToken();

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader(
                value = "Authorization",
                required = false
            ) String authorization) {

        if (authorization != null &&
                authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            tokenService.removeToken(token);
        }

        return ResponseEntity.ok(
                Map.of("message", "Logged out")
        );
    }
}