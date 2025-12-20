package com.lmt.Kanban.service;

import jakarta.servlet.http.HttpServletResponse;

public interface CookiesService {
    void addAccessToken(HttpServletResponse response, String token);
    void addRefreshToken(HttpServletResponse response, String token);
    void clearToken(HttpServletResponse response);

}
