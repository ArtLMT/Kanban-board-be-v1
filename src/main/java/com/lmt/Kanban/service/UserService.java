package com.lmt.Kanban.service;

import com.lmt.Kanban.dto.request.CreateUserRequest;
import com.lmt.Kanban.dto.response.UserResponse;
import com.lmt.Kanban.entity.User;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    User getUserEntity(Long userId);
}
