package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.dto.request.CreateBoardRequest;
import com.lmt.Kanban.dto.response.BoardResponse;
import com.lmt.Kanban.dto.response.StatusResponse;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.exception.ErrorCode;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.repository.BoardRepository;
import com.lmt.Kanban.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {
    private BoardRepository boardRepository;

    @Override
    public List<StatusResponse> getAllStatus(Long boardId) {

        return List.of();
    }

    @Override
    public BoardResponse createBoard(CreateBoardRequest request) {
        return null;
    }

    @Override
    public BoardResponse getBoardById(Long boardId) {
        validateBoardId(boardId);
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOARD_NOT_FOUND,"Board not found with ID: " + boardId));

        return createBoardResponse(board);
    }

    @Override
    public Board getBoardEntity(Long boardID) {
        validateBoardId(boardID);

        return boardRepository.findById(boardID).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOARD_NOT_FOUND,"Board not found with ID: " + boardID));
    }

    @Override
    public void deleteBoard(Long boardId) {

    }

    public void validateBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOARD_NOT_FOUND,"Board not found with ID: " + boardId));
    }

    private BoardResponse createBoardResponse(Board board) {
        return BoardResponse.builder()
                .title(board.getTitle())
                .build();
    }

    private void validateBoardId(Long boardId) {
        if (boardId == null) {
            throw new IllegalArgumentException("Board ID cannot be null");
        }
    }
}
