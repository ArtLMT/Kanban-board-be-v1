package com.lmt.Kanban.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class UpdateStatusRequest {
    @NotNull(message = "Status ID can't be empty")
    private Long id;

    private String name;
    private String color;
    private Integer position;
}
