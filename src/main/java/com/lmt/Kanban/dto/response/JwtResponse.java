package com.lmt.Kanban.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
//    private String type = "Bearer";
//    private Long id;
//    private String username;
//    private List<String> roles;
}
