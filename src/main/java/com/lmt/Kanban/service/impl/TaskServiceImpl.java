package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.dto.request.CreateTaskRequest;
import com.lmt.Kanban.dto.response.TaskResponse;
import com.lmt.Kanban.entity.Task;
import com.lmt.Kanban.exception.InvalidRequestException;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.repository.TaskRepository;
import com.lmt.Kanban.security.SecurityUtils;
import com.lmt.Kanban.service.BoardService;
import com.lmt.Kanban.service.StatusService;
import com.lmt.Kanban.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final BoardService boardService;
    private final StatusService statusService;

    private final SecurityUtils securityUtils;

    @Override
    public void deleteTask(Long taskId) {
        if (taskId == null) {
            throw new InvalidRequestException("Task ID cannot be null");
        }

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            throw new ResourceNotFoundException("Task not found");
        }

        taskRepository.delete(task);
    }

    @Override
    public TaskResponse getTaskIdByTaskId(Long taskId) {
        if (taskId == null) {
            throw new InvalidRequestException("Task ID cannot be null");
        }

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            throw new ResourceNotFoundException("Task not found");
        }

        return createTaskResponse(task);
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        validateRequest(request);

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCreator(securityUtils.getCurrentUser());
        task.setBoard(boardService.getBoardEntity(request.getBoardId()));
        task.setStatus(statusService.getStatusEntity(request.getStatusId()));
        taskRepository.save(task);
        return createTaskResponse(task);
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        List<Task> taskEntities = taskRepository.findAll();

        return taskEntities.stream()
                .map(this::createTaskResponse)
                .toList();
    }

    private TaskResponse createTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())

                // Cần viết lại chỗ này
                .boardId(task.getBoard() != null ? task.getBoard().getId() : null)
                .statusId(task.getStatus() != null ? task.getStatus().getId() : null)
                .creatorId(task.getCreator() != null ? task.getCreator().getId() : null)

                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .build();
    }

    private void validateRequest(CreateTaskRequest request) {
     if (request.getTitle() == null || request.getTitle().isBlank()) {
         throw new InvalidRequestException("Title cannot be null or blank");
     }

     if (request.getBoardId() == null) {
         throw new InvalidRequestException("Board ID cannot be null");
     }

     if (request.getStatusId() == null) {
         throw new InvalidRequestException("Status ID cannot be null");
     }

    }
}
