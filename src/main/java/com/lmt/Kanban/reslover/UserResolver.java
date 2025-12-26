package com.lmt.Kanban.reslover;

import com.lmt.Kanban.entity.User;
import com.lmt.Kanban.exception.ErrorCode;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResolver {
    private final UserRepository userRepository;

    public User findByIdOrThrow (Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                ErrorCode.USER_NOT_FOUND,
                "User not found: " + id
        ));
    }
}
