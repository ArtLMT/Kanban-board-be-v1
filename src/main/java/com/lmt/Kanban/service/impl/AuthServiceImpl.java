package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.dto.response.UserResponse;
import com.lmt.Kanban.entity.RefreshToken;
import com.lmt.Kanban.exception.ConflictException;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.repository.RefreshTokenRepository;
import com.lmt.Kanban.repository.UserRepository;
import com.lmt.Kanban.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;


import com.lmt.Kanban.dto.request.LoginRequest;
import com.lmt.Kanban.dto.request.RegisterRequest;
import com.lmt.Kanban.dto.response.JwtResponse;
import com.lmt.Kanban.entity.User;
import com.lmt.Kanban.service.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponse login(LoginRequest request) {
        // 1. Xác thực (Authentication)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), // Hoặc identifier nếu làm gộp
                        request.getPassword()
                )
        );

        // 2. Set Context (Optional nếu dùng JWT stateless, nhưng cứ giữ cũng được)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Lấy User từ DB
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 4. Sinh Token
        String accessToken = jwtUtils.generateJwtToken(user.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        // 5. Trả về data (Controller sẽ dùng data này để nhét vào Cookie)
        return new JwtResponse(accessToken, refreshToken.getToken(), user.getUsername());
    }

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already used");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }


    @Override
    public void logout(String refreshTokenValue) {
        refreshTokenRepository.findByToken(refreshTokenValue)
                .ifPresent(refreshTokenService::revokeToken);
    }


    @Override
    public JwtResponse refreshAccessToken(String refreshTokenValue) {
        // Tìm Token trong DB
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        // Check hết hạn
        if (refreshTokenService.isTokenExpired(refreshToken)) {
            refreshTokenService.revokeToken(refreshToken);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }

        // Sinh Access Token mới
        String newAccessToken = jwtUtils.generateJwtToken(refreshToken.getUser().getUsername());

        return new JwtResponse(newAccessToken, refreshTokenValue, refreshToken.getUser().getUsername());
    }

    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResourceNotFoundException("User not authenticated");
        }

        // 3. Lấy Entity
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 4. Convert sang DTO (UserResponse)
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .build();
    }
}
