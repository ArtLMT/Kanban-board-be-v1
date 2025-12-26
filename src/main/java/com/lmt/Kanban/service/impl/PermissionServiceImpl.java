package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.common.enums.BoardRole;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.entity.Task;
import com.lmt.Kanban.entity.User;
import com.lmt.Kanban.exception.ForbiddenException;
import com.lmt.Kanban.exception.InvalidRequestException;
import com.lmt.Kanban.repository.BoardMemberRepository;
import com.lmt.Kanban.security.SecurityUtils;
import com.lmt.Kanban.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final BoardMemberRepository boardMemberRepository;
    private final SecurityUtils securityUtils;

    @Override
    public void checkBoardMember(Long boardId) {
        User currentUser = securityUtils.getCurrentUser();
        if (!isBoardMember(boardId, currentUser.getId())) {
            throw new ForbiddenException("You are not a member of this board.");
        }
    }

    @Override
    public void checkBoardAdminOrOwner(Long boardId) {
        User currentUser = securityUtils.getCurrentUser();
        BoardRole role = getUserRole(boardId, currentUser.getId());

        if (role != BoardRole.OWNER && role != BoardRole.ADMIN) {
            throw new ForbiddenException("You don't have permission (Admin/Owner required).");
        }
    }


    @Override
    public void checkTaskView(Task task) {
        checkBoardMember(task.getBoard().getId());
    }

    @Override
    public void checkTaskUpdate(Task task) {
        User currentUser = securityUtils.getCurrentUser();
        Long boardId = task.getBoard().getId();
        BoardRole role = getUserRole(boardId, currentUser.getId());

        if (role == BoardRole.OWNER || role == BoardRole.ADMIN) {
            return;
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
    public void checkTaskDelete(Task task) {
        checkBoardAdminOrOwner(task.getBoard().getId());
    }

    @Override
    public void checkTaskAssign(Long boardId, Long assigneeId) {
        if (!isBoardMember(boardId, assigneeId)) {
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

