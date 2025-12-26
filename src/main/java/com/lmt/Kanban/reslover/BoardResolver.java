package com.lmt.Kanban.reslover;

import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.exception.ErrorCode;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardResolver {
    private final BoardRepository boardRepository;

    public Board findByIdOrThrow(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.BOARD_NOT_FOUND,
                        "Board not found: " + id
                ));
    }
}
