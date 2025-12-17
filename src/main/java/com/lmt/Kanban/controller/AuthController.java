package com.lmt.Kanban.controller;

//import com.lmt.Kanban.dto.*;
import com.lmt.Kanban.dto.request.LoginRequest;
import com.lmt.Kanban.dto.request.RefreshTokenRequest;
import com.lmt.Kanban.dto.request.RegisterRequest;
import com.lmt.Kanban.dto.response.JwtResponse;
import com.lmt.Kanban.entity.RefreshToken;
import com.lmt.Kanban.entity.User;
import com.lmt.Kanban.repository.RefreshTokenRepository;
import com.lmt.Kanban.repository.UserRepository;
import com.lmt.Kanban.security.JwtUtils;
import com.lmt.Kanban.service.impl.RefreshTokenServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    // ===================== LOGIN =====================
    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtUtils.generateJwtToken(user.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new JwtResponse(accessToken, refreshToken.getToken());
    }

    // ===================== REFRESH TOKEN =====================
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshTokenService.isTokenExpired(refreshToken)) {
            refreshTokenService.revokeToken(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        String newAccessToken = jwtUtils.generateJwtToken(
                refreshToken.getUser().getUsername()
        );

        return ResponseEntity.ok(
                new JwtResponse(newAccessToken, refreshToken.getToken())
        );
    }

    // ===================== LOGOUT =====================
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenService.revokeToken(refreshToken);

        return ResponseEntity.ok("Logged out successfully");
    }

    //====================== REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already used");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
//        return JwtResponse(
//
//        )
    }

}
