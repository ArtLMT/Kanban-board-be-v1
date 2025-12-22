package com.lmt.Kanban.mapper;

import com.lmt.Kanban.dto.request.UpdateTaskRequest;
import com.lmt.Kanban.entity.Task;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring", // Để Spring quản lý như một Bean (@Autowired được)
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE // <--- CÂU THẦN CHÚ: Null thì lờ đi, không set
)
public interface TaskMapper {

    // Hàm này dùng để map từ DTO update vào Entity có sẵn
    // @MappingTarget báo cho MapStruct biết "hãy update vào thằng entity này"

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
