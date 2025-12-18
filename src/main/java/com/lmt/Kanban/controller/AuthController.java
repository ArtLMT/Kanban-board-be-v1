package com.lmt.Kanban.controller;

//import com.lmt.Kanban.dto.*;
import com.lmt.Kanban.dto.request.LoginRequest;
import com.lmt.Kanban.dto.request.RefreshTokenRequest;
import com.lmt.Kanban.dto.request.RegisterRequest;
import com.lmt.Kanban.dto.response.JwtResponse;
import com.lmt.Kanban.entity.CustomUserDetails;
import com.lmt.Kanban.entity.RefreshToken;
import com.lmt.Kanban.entity.User;
import com.lmt.Kanban.exception.GlobalExceptionHandler;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.repository.RefreshTokenRepository;
import com.lmt.Kanban.repository.UserRepository;
import com.lmt.Kanban.security.JwtUtils;
import com.lmt.Kanban.service.impl.RefreshTokenServiceImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String accessToken = jwtUtils.generateJwtToken(user.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        // ================= COOKIES =================

        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false); // true in prod (HTTPS)
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60);

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken.getToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/api/auth/refresh");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        // ================= RESPONSE BODY =================
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new JwtResponse(accessToken, refreshToken.getToken(), user.getUsername()));
    }


    // ===================== REFRESH TOKEN =====================
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshTokenValue,
            HttpServletResponse response
    ) {
        if (refreshTokenValue == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshTokenService.isTokenExpired(refreshToken)) {
            refreshTokenService.revokeToken(refreshToken);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken =
                jwtUtils.generateJwtToken(refreshToken.getUser().getUsername());

        Cookie accessCookie = new Cookie("accessToken", newAccessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60);

        response.addCookie(accessCookie);

        return ResponseEntity.ok(
                new JwtResponse(newAccessToken, refreshTokenValue, userRepository.findByUsername(refreshToken.getUser().getUsername()).get().getUsername())
        );
    }


    // ===================== LOGOUT =====================
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        Cookie accessCookie = new Cookie("accessToken", null);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);

        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setPath("/api/auth/refresh");
        refreshCookie.setMaxAge(0);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return ResponseEntity.noContent().build();
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

    //=====================
        @GetMapping("/me")
        public ResponseEntity<?> getCurrentUser(Authentication authentication) {

            if (authentication == null ||
                    authentication.getPrincipal().equals("anonymousUser")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

//        User user = (User) authentication.getPrincipal(); // Thằng userDetail này kh phải User entity
            CustomUserDetails userDetails =
                    (CustomUserDetails) authentication.getPrincipal();
            // Vì thế nên mới chơi cái trò này
            User user = userDetails.getUser();

            return ResponseEntity.ok(
                    Map.of(
                            "id", user.getId(),
                            "username", user.getUsername(),
                            "email", user.getEmail()
                    )
            );
        }

}
