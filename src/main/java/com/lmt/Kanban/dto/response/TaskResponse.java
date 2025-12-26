package com.lmt.Kanban.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;

    private Long statusId;
    private Long boardId;
    private Long creatorId;

    private Long assigneeId;
}
