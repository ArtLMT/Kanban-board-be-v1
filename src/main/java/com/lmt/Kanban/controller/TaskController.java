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
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("")
    public ResponseEntity<List<TaskResponse>> getAllTasksByBoardId(@RequestParam Long boardId) {
        return ResponseEntity.ok(taskService.getAllTasks(boardId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping("")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(request));
    }

    @PutMapping("{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTask(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}

//    @GetMapping("")
//    public ResponseEntity<List<TaskResponse>> getAllTasks() {
//        return ResponseEntity.ok(taskService.getAllTasks());
//    }