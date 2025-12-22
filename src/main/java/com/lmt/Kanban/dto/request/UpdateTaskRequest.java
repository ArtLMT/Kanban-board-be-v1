package com.lmt.Kanban.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTaskRequest {
    @NotNull(message = "Task ID can't be empty")
    private Long id;

    @Pattern(regexp = "^(?!\\s*$).+", message = "Title cannot be empty")
    @Schema(description = "Title of the task", example = "Task 1")
    private String title;
    private String description;

    private Long statusId;
    private Long boardId;

    private Long assigneeId;
}
