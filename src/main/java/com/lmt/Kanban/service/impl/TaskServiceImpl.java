package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.dto.request.CreateTaskRequest;
import com.lmt.Kanban.dto.request.UpdateTaskRequest;
import com.lmt.Kanban.dto.response.TaskResponse;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.entity.Status;
import com.lmt.Kanban.entity.Task;
import com.lmt.Kanban.exception.ErrorCode;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.mapper.TaskMapper;
import com.lmt.Kanban.repository.TaskRepository;
import com.lmt.Kanban.security.SecurityUtils;
import com.lmt.Kanban.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final BoardService boardService;
    private final StatusService statusService;
    private final PermissionService permissionService;

    private final SecurityUtils securityUtils;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @Override
    public void deleteTask(Long taskId) {

        Task task = getTaskEntity(taskId);

        permissionService.checkTaskDelete(task);

        // Nhỉn vậy thôi chứ do bên Entity đã cấu hình soft delete rồi
        taskRepository.delete(task);
    }

    @Override
    public TaskResponse getTaskById(Long taskId) {
        Task task = getTaskEntity(taskId);

        return taskMapper.toResponse(task);
    }

    @Override
    public TaskResponse createTask(Board board, Status status, CreateTaskRequest request) {
        permissionService.checkBoardAdminOrOwner(board.getId());

        Task task = taskMapper.toEntity(request);

        task.setBoard(board);
        task.setStatus(status);
        task.setCreator(securityUtils.getCurrentUser());

        if (request.getAssigneeId() != null && request.getAssigneeId() != 0) {
            task.setAssignee(userService.getUserEntity(request.getAssigneeId()));
        }

        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        List<Task> taskEntities = taskRepository.findAll();

        return taskEntities.stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional // Thằng này rất quan trọng nếu mà phải chỉnh sửa nhiều query trong 1 chuỗi hành đông
    // Giải thích dễ hiểu thì nếu A chuyển tiền sang B, quy trình sẽ gồm - tiền của A, xong + tiền của B
    // Nếu mà A vừa trừ tiền xong gặp lỗi mạng -> B kh chạy được -> B kh nhận được tiền
    // Thằng Transactional này sẽ giúp A kh bị trừ tiền nếu mà B bị lỗi
    // Chỉ khi nào toàn bộ đều thành công thì mới ok, còn fail thì phải fail hết
    public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {
        Task task = getTaskEntity(taskId);

        permissionService.checkTaskUpdate(task);

        // Nếu request.Title = null -> MapStruct giữ nguyên Title cũ.
        // Nếu request.Title = "Mới" -> MapStruct update thành "Mới".
        taskMapper.updateTaskFromDto(request, task);

        if (request.getStatusId() != null && !request.getStatusId().equals(task.getStatus().getId())) {
            Status newStatus = statusService.validateStatusInBoard(request.getStatusId(), task.getBoard().getId());
            task.setStatus(newStatus);
        }

        if (request.getAssigneeId() != null) {
            Long currentAssigneeId = (task.getAssignee() != null) ? task.getAssignee().getId() : null;

            if (!request.getAssigneeId().equals(currentAssigneeId)) {

                if (request.getAssigneeId() == 0) {
                    task.setAssignee(null);
                } else {
                    permissionService.checkTaskAssign(task.getBoard().getId(), request.getAssigneeId());
                    task.setAssignee(userService.getUserEntity(request.getAssigneeId()));
                }
            }
        }

        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Override
    public List<TaskResponse> getAllTasks(Long boardId) {
        List<Task> tasks = taskRepository.findByBoardId(boardId);

        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getAllTasks(Long boardId, Long statusId) {
        statusService.validateStatusInBoard(statusId, boardId);
        List<Task> tasks = taskRepository.findByBoardIdAndStatusId(boardId, statusId);

        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    private Task getTaskEntity(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TASK_NOT_FOUND, "Task not found with ID: " + taskId));
    }
}
