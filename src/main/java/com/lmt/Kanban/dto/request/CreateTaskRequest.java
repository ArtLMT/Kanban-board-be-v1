package com.lmt.Kanban.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTaskRequest {
    @NotBlank(message = "Title can't be null")
    private String title;
    private String description;

    @NotNull(message = "Status ID can't be empty")
    private Long statusId;

    @NotNull(message = "Board ID can't be null")
    private Long boardId;

    private Long assigneeId;
}
