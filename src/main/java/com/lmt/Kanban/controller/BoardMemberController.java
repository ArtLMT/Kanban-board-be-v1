package com.lmt.Kanban.controller;

import com.lmt.Kanban.common.enums.BoardRole;
import com.lmt.Kanban.dto.request.CreateBoardMemberRequest;
import com.lmt.Kanban.dto.response.MemberResponse;
import com.lmt.Kanban.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards/{boardId}/members")
@RequiredArgsConstructor
public class BoardMemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers(@PathVariable Long boardId) {
        return ResponseEntity.ok(memberService.getBoardMembers(boardId));
    }

    @PostMapping
    public ResponseEntity<MemberResponse> addMember(
            @PathVariable Long boardId,
            @Valid @RequestBody CreateBoardMemberRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberService.createBoardMember(boardId, request));
    }

    // PUT /boards/{boardId}/members/{userId}?role=ADMIN
    @PutMapping("/{userId}")
    public ResponseEntity<MemberResponse> updateMemberRole(
            @PathVariable Long boardId,
            @PathVariable Long userId,
            @RequestParam BoardRole role) {
        return ResponseEntity.ok(memberService.updateBoardMember(boardId, userId, role));
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> removeMember(
            @PathVariable Long boardId,
            @PathVariable Long userId) {
        memberService.removeBoardMember(boardId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/leave")
    public ResponseEntity<?> leaveBoard(@PathVariable Long boardId) {
        memberService.leaveBoard(boardId);
        return ResponseEntity.noContent().build();
    }
}