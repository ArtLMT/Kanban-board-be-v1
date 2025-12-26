package com.lmt.Kanban.mapper;

import com.lmt.Kanban.dto.request.CreateBoardRequest;
import com.lmt.Kanban.dto.request.UpdateBoardRequest;
import com.lmt.Kanban.dto.response.BoardResponse;
import com.lmt.Kanban.entity.Board;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BoardMapper {
    Board toEntity(CreateBoardRequest request);

    BoardResponse toResponse(Board board);

    void updateBoardFromRequest(UpdateBoardRequest request, @MappingTarget Board board);}
