package com.lmt.Kanban.controller;

import com.lmt.Kanban.dto.request.CreateBoardRequest;
import com.lmt.Kanban.dto.request.UpdateBoardRequest;
import com.lmt.Kanban.dto.response.BoardResponse;
import com.lmt.Kanban.service.BoardService;
import com.lmt.Kanban.service.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // Mình phải thông qua BoardMember để lấy toàn bộ board ra, kể cả board mà user không phải OWNER, lấy cả MEMBER, ADMIN, VIEWER luôn mới hợp lý
    @GetMapping("")
    public ResponseEntity<List<BoardResponse>> getMyBoards() {
        return ResponseEntity.ok(boardService.getMyBoards());
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoardById(boardId));
    }

    @PostMapping("")
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createBoard(request));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long boardId, @RequestBody UpdateBoardRequest request) {
        return ResponseEntity.ok(boardService.updateBoard(boardId ,request));
    }
}
