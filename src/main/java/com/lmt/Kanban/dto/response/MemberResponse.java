package com.lmt.Kanban.dto.response;

import com.lmt.Kanban.common.enums.BoardRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MemberResponse {
    private Long id;
    private String username;
    private String email;

    private BoardRole role;
    private LocalDateTime joinTime;
}
