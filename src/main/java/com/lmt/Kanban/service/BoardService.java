package com.lmt.Kanban.service;

import com.lmt.Kanban.dto.request.CreateBoardRequest;
import com.lmt.Kanban.dto.request.UpdateBoardRequest;
import com.lmt.Kanban.dto.response.BoardResponse;
import com.lmt.Kanban.dto.response.StatusResponse;
import com.lmt.Kanban.entity.Board;


import java.util.List;

public interface BoardService {
    List<BoardResponse> getMyBoards();
    BoardResponse createBoard(CreateBoardRequest request);
    BoardResponse getBoardById(Long boardId);
    BoardResponse updateBoard(Long boardId, UpdateBoardRequest request);
    void deleteBoard(Long boardId);
}
