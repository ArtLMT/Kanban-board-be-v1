package com.lmt.Kanban.mapper;

import com.lmt.Kanban.dto.request.CreateStatusRequest;
import com.lmt.Kanban.dto.request.UpdateStatusRequest;
import com.lmt.Kanban.dto.response.StatusResponse;
import com.lmt.Kanban.entity.Status;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StatusMapper {
    @Mapping(target = "boardId", source = "board.id")
    StatusResponse toResponse(Status status);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "board", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "color", source = "color", qualifiedByName = "mapColorDefault")
    Status toEntity(CreateStatusRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "board", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "position", ignore = true)
    void updateStatusFromRequest(UpdateStatusRequest request, @MappingTarget Status status);

    @Named("mapColorDefault")
    default String mapColorDefault(String color) {
        if (color == null || color.trim().isEmpty()) {
            return "#F5F5F5";
        }
        return color;
    }
}
