package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.dto.request.CreateTaskRequest;
import com.lmt.Kanban.dto.request.UpdateTaskRequest;
import com.lmt.Kanban.dto.response.TaskResponse;
import com.lmt.Kanban.entity.Status;
import com.lmt.Kanban.entity.Task;
import com.lmt.Kanban.exception.ErrorCode;
import com.lmt.Kanban.exception.InvalidRequestException;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.mapper.TaskMapper;
import com.lmt.Kanban.repository.TaskRepository;
import com.lmt.Kanban.security.SecurityUtils;
import com.lmt.Kanban.service.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final BoardService boardService;
    private final StatusService statusService;
    private final TaskPermissionService taskPermissionService;

    private final SecurityUtils securityUtils;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @Override
    public void deleteTask(Long taskId) {
        if (taskId == null) {
            throw new InvalidRequestException("Task ID cannot be null");
        }

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TASK_NOT_FOUND,"Task not found with ID: " + taskId));

        taskPermissionService.checkCanDelete(task);

        taskRepository.delete(task);
//        task.setDeleted(true);
//        taskRepository.save(task);
    }

    @Override
    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TASK_NOT_FOUND,"Task not found with ID: " + taskId));

        return createTaskResponse(task);
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        taskPermissionService.checkCanCreate(boardService.getBoardEntity(request.getBoardId()));

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

    @Override
    @Transactional // Thằng này rất quan trọng nếu mà phải chỉnh sửa nhiều query trong 1 chuỗi hành đông
    // Giải thích dễ hiểu thì nếu A chuyển tiền sang B, quy trình sẽ gồm - tiền của A, xong + tiền của B
    // Nếu mà A vừa trừ tiền xong gặp lỗi mạng -> B kh chạy được -> B kh nhận được tiền
    // Thằng Transactional này sẽ giúp A kh bị trừ tiền nếu mà B bị lỗi
    // Chỉ khi nào toàn bộ đều thành công thì mới ok, còn fail thì phải fail hết
    public TaskResponse updateTask(UpdateTaskRequest request) {
        Task task = taskRepository.findById(request.getId()).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TASK_NOT_FOUND,"Task not found with ID: " + request.getId()));

        taskPermissionService.checkCanUpdate(task);

        // Nếu request.Title = null -> MapStruct giữ nguyên Title cũ.
        // Nếu request.Title = "Mới" -> MapStruct update thành "Mới".
        taskMapper.updateTaskFromDto(request, task);

        if (request.getStatusId() != null) {
            if (!request.getStatusId().equals(task.getStatus().getId())) {

                Status newStatus = statusService.getStatusEntity(request.getStatusId());

                if (!newStatus.getBoard().getId().equals(task.getBoard().getId())) {
                    throw new InvalidRequestException("Status " + request.getStatusId() + " does not belong to Board " + task.getBoard().getId());
                }

                task.setStatus(newStatus);
            }
        }

        if (request.getAssigneeId() != null) {
            Long currentAssigneeId = (task.getAssignee() != null) ? task.getAssignee().getId() : null;

            if (!request.getAssigneeId().equals(currentAssigneeId)) {

                if (request.getAssigneeId() == 0) {
                    task.setAssignee(null);
                } else {
                    task.setAssignee(userService.getUserEntity(request.getAssigneeId()));
                }
            }
        }

        return createTaskResponse(taskRepository.save(task));
    }

    private TaskResponse createTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .boardId(task.getBoard().getId())
                .statusId(task.getStatus().getId())
                .creatorId(task.getCreator().getId())
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .build();
    }
}
