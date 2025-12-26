package com.lmt.Kanban.controller;

import com.lmt.Kanban.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boards/{boardId}/members")
@RequiredArgsConstructor
public class BoardMemberController {
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers(@PathVariable Long boardId) {
        return ResponseEntity.ok(memberService.getMembers(boardId));


}
