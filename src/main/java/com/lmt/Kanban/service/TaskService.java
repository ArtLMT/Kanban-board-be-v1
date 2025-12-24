package com.lmt.Kanban.service;

import com.lmt.Kanban.dto.request.CreateTaskRequest;
import com.lmt.Kanban.dto.request.UpdateTaskRequest;
import com.lmt.Kanban.dto.response.TaskResponse;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.entity.Status;
import com.lmt.Kanban.entity.Task;

import java.util.List;

public interface TaskService {
    void deleteTask(Long taskId);
    TaskResponse getTaskById(Long taskId);
    TaskResponse createTask(Board board, Status status, CreateTaskRequest request);
    List<TaskResponse> getAllTasks();
    TaskResponse updateTask(UpdateTaskRequest request);
}
