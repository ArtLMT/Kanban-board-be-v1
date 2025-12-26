package com.lmt.Kanban.mapper;

import com.lmt.Kanban.dto.request.CreateTaskRequest;
import com.lmt.Kanban.dto.request.UpdateTaskRequest;
import com.lmt.Kanban.dto.response.TaskResponse;
import com.lmt.Kanban.entity.Task;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring", // Để Spring quản lý như một Bean (@Autowired được)
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE // <--- CÂU THẦN CHÚ: Null thì lờ đi, không set
)
public interface TaskMapper {
    @Mapping(target = "boardId", source = "board.id")
    @Mapping(target = "statusId", source = "status.id")
    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "assigneeId", source = "assignee.id")
    TaskResponse toResponse(Task task);

    // Hàm này dùng để map từ DTO update vào Entity có sẵn
    // @MappingTarget báo cho MapStruct biết "hãy update vào thằng entity này"
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "board", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "creator", ignore = true) 
    Task toEntity(CreateTaskRequest request);

    // MapStruct rất giỏi map String -> String.
    // Nhưng nó không tự map Long (id) -> Status (Entity) được nếu không dạy nó.
    // Tạm thời ta IGNORE (bỏ qua) các field ID để xử lý tay ở Service cho an toàn logic
    @Mapping(target = "id", ignore = true)       // Không cho sửa ID
    @Mapping(target = "board", ignore = true)   // Mấy relationship này mà chưa gì để tự sửa thì chặt tay đi
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "creator", ignore = true)

    void updateTaskFromDto(UpdateTaskRequest request, @MappingTarget Task task);

}
