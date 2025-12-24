package com.lmt.Kanban.service;

import com.lmt.Kanban.dto.request.CreateBoardRequest;
import com.lmt.Kanban.dto.response.BoardResponse;
import com.lmt.Kanban.dto.response.StatusResponse;
import com.lmt.Kanban.entity.Board;


import java.util.List;

public interface BoardService {
    List<StatusResponse> getAllStatus(Long boardId);
    BoardResponse createBoard(CreateBoardRequest request);
    BoardResponse getBoardById(Long boardId);
    Board getBoardEntity(Long boardID);
    void validateBoard(Long boardId);
    void deleteBoard(Long boardId);
}
