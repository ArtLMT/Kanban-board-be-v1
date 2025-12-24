package com.lmt.Kanban.controller;

import com.lmt.Kanban.dto.response.BoardResponse;
import com.lmt.Kanban.dto.response.StatusResponse;
import com.lmt.Kanban.service.BoardService;
import com.lmt.Kanban.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final StatusService statusService;



    @GetMapping("/{boardID}/statuses")
    public ResponseEntity<List<StatusResponse>> getAllStatusByBoardId(@PathVariable Long boardID) {
//        BoardResponse board = boardService.getBoardById(boardID);

        List<StatusResponse> statuses = statusService.getAllStatus(boardID);

        return ResponseEntity.ok(statuses);
    }
}
