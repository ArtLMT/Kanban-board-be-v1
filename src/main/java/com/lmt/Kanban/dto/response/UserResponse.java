package com.lmt.Kanban.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserResponse implements Serializable {
    private Long id;
    private String username;
    private String displayName;
    private String email;
}
