package com.lmt.Kanban.controller;

import com.lmt.Kanban.dto.request.CreateTaskRequest;
import com.lmt.Kanban.dto.request.UpdateTaskRequest;
import com.lmt.Kanban.dto.response.TaskResponse;
import com.lmt.Kanban.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/getAllTasks")
    public List<ResponseEntity<TaskResponse>> getAllTasks() {
        return taskService.getAllTasks().stream().map(ResponseEntity::ok).toList();
    }

    @PostMapping("")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(request));
    }

    @PutMapping("")
    public ResponseEntity<TaskResponse> updateTask(@Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(request));
    }
}
