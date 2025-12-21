package com.lmt.Kanban.service;

import com.lmt.Kanban.dto.request.LoginRequest;
import com.lmt.Kanban.dto.request.RegisterRequest;
import com.lmt.Kanban.dto.response.JwtResponse;
import com.lmt.Kanban.dto.response.UserResponse;
import com.lmt.Kanban.entity.User;

public interface AuthService {
    JwtResponse login(LoginRequest request);
    JwtResponse refreshAccessToken(String refreshToken);
    void logout(String refreshToken);
    void register(RegisterRequest request);
    UserResponse getCurrentUser(); // Helper để lấy user info
}
