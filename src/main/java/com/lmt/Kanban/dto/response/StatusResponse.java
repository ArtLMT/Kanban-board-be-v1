package com.lmt.Kanban.dto.response;

import lombok.*;

@Data
//@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {
    private Long id;
    private String name;
    private String color;

    private Integer position;
    private Long boardId;
}
