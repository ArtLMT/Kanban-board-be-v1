package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.common.enums.BoardRole;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.entity.Task;
import com.lmt.Kanban.entity.User;
import com.lmt.Kanban.exception.ForbiddenException;
import com.lmt.Kanban.exception.InvalidRequestException;
import com.lmt.Kanban.repository.BoardMemberRepository;
import com.lmt.Kanban.security.SecurityUtils;
import com.lmt.Kanban.service.TaskPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskPermissionServiceImpl implements TaskPermissionService {

    private final BoardMemberRepository boardMemberRepository;
    private final SecurityUtils securityUtils;

    @Override
    public void checkCanView(Task task) {
        User currentUser = securityUtils.getCurrentUser();

        if (!isBoardMember(task.getBoard().getId(), currentUser.getId())) {
            throw new ForbiddenException("You are not a member of this board. You can't view tasks in this board.");
        }
    }

    @Override
    public void checkCanCreate(Board board) {
        User currentUser = securityUtils.getCurrentUser();

        BoardRole role = getUserRole(board.getId(), currentUser.getId());

        if (role != BoardRole.OWNER && role != BoardRole.ADMIN) {
            throw new ForbiddenException("You don't have permission to create task in this board");
        }
    }


    @Override
    public void checkCanUpdate(Task task) {
        User currentUser = securityUtils.getCurrentUser();

        BoardRole role = getUserRole(task.getBoard().getId(), currentUser.getId());

        if (role == null) {
            throw new ForbiddenException("You are not a board member");
        }

        if (role == BoardRole.MEMBER) {
            boolean isCreator = task.getCreator().getId().equals(currentUser.getId());
            boolean isAssignee = task.getAssignee() != null
                    && task.getAssignee().getId().equals(currentUser.getId());

            if (!isCreator && !isAssignee) {
                throw new ForbiddenException("You don't have permission to update this task");
            }
        }
    }

    @Override
    public void checkCanDelete(Task task) {
        User currentUser = securityUtils.getCurrentUser();

        BoardRole role = getUserRole(task.getBoard().getId(), currentUser.getId());

        if (role == null) {
            throw new ForbiddenException("You are not a board member");
        }


        if (role != BoardRole.OWNER && role != BoardRole.ADMIN) {
            throw new ForbiddenException("Only owner or admin can delete task");
        }
    }

    @Override
    public void checkCanAssign(Task task, User assignee) {
        if (!isBoardMember(task.getBoard().getId(), assignee.getId())) {
            throw new InvalidRequestException("Assignee is not a board member");
        }
    }

    private boolean isBoardMember(Long boardId, Long userId) {
        return boardMemberRepository.existsByBoardIdAndUserId(boardId, userId);
    }

    private BoardRole getUserRole(Long boardId, Long userId) {
        return boardMemberRepository
                .findRoleByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() -> new ForbiddenException("You are not a board member"));
    }
}

