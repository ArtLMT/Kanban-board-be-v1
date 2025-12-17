package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.entity.User;
import com.lmt.Kanban.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Thằng này sẽ được gọi thi mà login
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // Chỉ lấy User ra thôi, rồi dùng User đó để authenticate
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}

//TODO Thằng này sẽ dùng để kiểm tra user, quan trọng nhất là nó implement thằng UserDetailsService
// Nó sẽ giúp lấy thông tin của User ra để Spring Security có thể kiểm tra