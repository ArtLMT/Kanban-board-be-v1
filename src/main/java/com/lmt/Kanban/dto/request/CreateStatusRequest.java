package com.lmt.Kanban.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateStatusRequest {
    private String name;
    private String color;
    private Integer position;

    @NotNull(message = "Board ID can't be null")
    private Long boardId;

}
