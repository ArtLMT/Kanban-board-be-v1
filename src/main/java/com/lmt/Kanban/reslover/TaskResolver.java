package com.lmt.Kanban.reslover;

import com.lmt.Kanban.entity.Task;
import com.lmt.Kanban.exception.ErrorCode;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskResolver {
    private final TaskRepository taskRepository;

    public Task findByIdOrThrow(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                ErrorCode.TASK_NOT_FOUND,
                "Task not found: " + id
        ));
    }

}
