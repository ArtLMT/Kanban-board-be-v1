package com.lmt.Kanban.service;

import com.lmt.Kanban.dto.request.CreateTaskRequest;
import com.lmt.Kanban.dto.request.UpdateTaskRequest;
import com.lmt.Kanban.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {
    void deleteTask(Long taskId);
    TaskResponse getTaskById(Long taskId);
    TaskResponse createTask(CreateTaskRequest request);
    TaskResponse updateTask( Long taskId,UpdateTaskRequest request);
    List<TaskResponse> getAllTasks(Long statusId);
}

//    List<TaskResponse> getAllTasks(Long boardId, Long statusId);
//    List<TaskResponse> getAllTasks();