package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.dto.request.CreateUserRequest;
import com.lmt.Kanban.dto.response.UserResponse;
import com.lmt.Kanban.entity.User;
import com.lmt.Kanban.exception.ErrorCode;
import com.lmt.Kanban.exception.InvalidRequestException;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.repository.UserRepository;
import com.lmt.Kanban.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        return null;
    }

    @Override
    public User getUserEntity(Long userId) {
        validateUserId(userId);

        User user = userRepository.findById(userId).orElse(null);
        validateUser(user);
        return user;
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new InvalidRequestException("User Id can't be null");
        }
    }

    private void validateUser(User user) {
        if (user == null ) {
            throw new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND,"User not found!");
        }
    }
}
