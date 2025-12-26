package com.lmt.Kanban.service;

import com.lmt.Kanban.common.enums.BoardRole;
import com.lmt.Kanban.dto.request.CreateBoardMemberRequest;
import com.lmt.Kanban.dto.response.MemberResponse;

import java.util.List;


public interface MemberService {
    List<MemberResponse> getBoardMembers(Long boardId);
    MemberResponse createBoardMember(Long boardId, CreateBoardMemberRequest request);
    MemberResponse updateBoardMember(Long boardId, Long userId, BoardRole role);
    void removeBoardMember(Long boardId, Long userId);
    void leaveBoard(Long boardId);
}
