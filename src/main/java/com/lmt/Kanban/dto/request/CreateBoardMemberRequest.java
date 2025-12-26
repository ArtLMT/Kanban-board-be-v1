package com.lmt.Kanban.dto.request;

import com.lmt.Kanban.common.enums.BoardRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBoardMemberRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private BoardRole role;

//    private Long userId;
}
