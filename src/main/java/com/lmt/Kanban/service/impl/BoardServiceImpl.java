package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.common.enums.BoardRole;
import com.lmt.Kanban.dto.request.CreateBoardRequest;
import com.lmt.Kanban.dto.request.UpdateBoardRequest;
import com.lmt.Kanban.dto.response.BoardResponse;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.entity.BoardMember;
import com.lmt.Kanban.entity.User;
import com.lmt.Kanban.exception.ErrorCode;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.mapper.BoardMapper;
import com.lmt.Kanban.repository.BoardMemberRepository;
import com.lmt.Kanban.repository.BoardRepository;
import com.lmt.Kanban.security.SecurityUtils;
import com.lmt.Kanban.service.BoardService;
import com.lmt.Kanban.service.PermissionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;

    private final SecurityUtils securityUtils;
    private final BoardMapper boardMapper;
    private final PermissionService permissionService;

    @Override
    public List<BoardResponse> getMyBoards() {
        Long userId = securityUtils.getCurrentUser().getId();
        List<Board> boards = boardRepository.findAllBoardsByUserId(userId);

        return boards.stream()
                .map(boardMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BoardResponse createBoard(CreateBoardRequest request) {
        User currentUser = securityUtils.getCurrentUser();

        Board board = boardMapper.toEntity(request);

        board = boardRepository.save(board);

        BoardMember member = new BoardMember();
        member.setBoard(board);
        member.setUser(currentUser);
        member.setRole(BoardRole.OWNER);

        boardMemberRepository.save(member);

        return boardMapper.toResponse(board);
        }

    @Override
    public BoardResponse getBoardById(Long boardId) {
        permissionService.checkBoardMember(boardId);

        Board board = getBoardEntity(boardId);

        return boardMapper.toResponse(board);
    }

    @Override
    public void deleteBoard(Long boardId) {
        permissionService.checkBoardAdminOrOwner(boardId);

        boardRepository.deleteById(boardId);
    }

    @Override
    public BoardResponse updateBoard(Long boardId, UpdateBoardRequest request) {
        Board board = getBoardEntity(boardId);

        permissionService.checkBoardAdminOrOwner(boardId);

        boardMapper.updateBoardFromRequest(request, board);

        return boardMapper.toResponse(boardRepository.save(board));
    }

    private Board getBoardEntity(Long boardID) {
        return boardRepository.findById(boardID).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOARD_NOT_FOUND,"Board not found with ID: " + boardID));
    }
}
