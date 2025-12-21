package com.lmt.Kanban.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CreateBoardRequest implements Serializable {

    private String title;

}
