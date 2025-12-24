package com.lmt.Kanban.controller;

import com.lmt.Kanban.dto.request.CreateStatusRequest;
import com.lmt.Kanban.dto.request.UpdateStatusRequest;
import com.lmt.Kanban.dto.response.StatusResponse;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.service.BoardService;
import com.lmt.Kanban.service.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statuses")
@RequiredArgsConstructor
public class StatusController {
    private final StatusService statusService;
    private final BoardService boardService;

//    @GetMapping("")
//    public ResponseEntity<List<StatusResponse>> getAllStatuses() {
//        return ResponseEntity.ok(statusService.getAllStatus());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse> getStatusById(@PathVariable Long id) {
        return ResponseEntity.ok(statusService.getStatusById(id));
    }

    @PostMapping("")
    public ResponseEntity<StatusResponse> createStatus(@Valid @RequestBody CreateStatusRequest request) {
        Board board = boardService.getBoardEntity(request.getBoardId());
        return ResponseEntity.status(HttpStatus.CREATED).body(statusService.createStatus(board , request));
    }

    // Khứa này chỉ dùng cho update field đơn giản thôi, còn position thì phải tạo cái riêng
    // API đổi tên, đổi màu
    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {

//        request.setId(id); // Set ID từ path vào request
        return ResponseEntity.ok().body(statusService.updateStatus(id, request));
    }

    // API kéo thả (Drag & Drop)
    @PatchMapping("/{id}/move")
    public ResponseEntity<?> moveStatus(
            @PathVariable Long id,
            @RequestParam Integer newPosition) {

        statusService.movePosition(id, newPosition);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}
