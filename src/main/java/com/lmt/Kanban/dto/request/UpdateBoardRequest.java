package com.lmt.Kanban.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateBoardRequest {
    @NotNull(message = "Board ID can't be empty")
    private String title;
}
