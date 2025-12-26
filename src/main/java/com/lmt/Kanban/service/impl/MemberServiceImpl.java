package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.common.enums.BoardRole;
import com.lmt.Kanban.dto.request.CreateBoardMemberRequest;
import com.lmt.Kanban.dto.response.MemberResponse;
import com.lmt.Kanban.dto.response.UserResponse;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.entity.BoardMember;
import com.lmt.Kanban.entity.User;
import com.lmt.Kanban.exception.ErrorCode;
import com.lmt.Kanban.exception.InvalidRequestException;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.mapper.MemberMapper;
import com.lmt.Kanban.repository.BoardMemberRepository;
import com.lmt.Kanban.repository.UserRepository;
import com.lmt.Kanban.reslover.BoardResolver;
import com.lmt.Kanban.security.SecurityUtils;
import com.lmt.Kanban.service.MemberService;
import com.lmt.Kanban.service.PermissionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final BoardMemberRepository memberRepository;
    private final UserRepository userRepository; // Mốt đổi thành UserResolver
    private final BoardResolver boardResolver;

    private final PermissionService permissionService;
    private final SecurityUtils securityUtils;
    private final MemberMapper memberMapper;

    @Override
    public List<MemberResponse> getBoardMembers(Long boardId) {
        boardResolver.findByIdOrThrow(boardId);

        permissionService.checkBoardMember(boardId);

        List<BoardMember> members = memberRepository.findByBoardId(boardId);
        return members.stream()
                .map(memberMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public MemberResponse createBoardMember(Long boardId, CreateBoardMemberRequest request) {
        permissionService.checkBoardAdminOrOwner(boardId);

        Board board = boardResolver.findByIdOrThrow(boardId);

        User userToAdd = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND,"User not found with email: " + request.getEmail()));

        Optional<BoardMember> existingMemberOpt = memberRepository.findMemberIncludeDeleted(boardId, userToAdd.getId());

        BoardMember memberToSave;

        if (existingMemberOpt.isPresent()) {
            memberToSave = existingMemberOpt.get();

            if (!memberToSave.isDeleted()) {
                throw new InvalidRequestException("User is already a member of this board");
            }

            memberToSave.setDeleted(false);
            memberToSave.setRole(request.getRole() != null ? request.getRole() : BoardRole.MEMBER);
        } else {
            memberToSave = new BoardMember();
            memberToSave.setBoard(board);
            memberToSave.setUser(userToAdd);
            memberToSave.setRole(request.getRole() != null ? request.getRole() : BoardRole.MEMBER);
            memberToSave.setDeleted(false);
        }

        return memberMapper.toResponse(memberRepository.save(memberToSave));
    }

    @Override
    @Transactional
    public MemberResponse updateBoardMember(Long boardId, Long userId, BoardRole newRole) {
        permissionService.checkBoardAdminOrOwner(boardId);

        BoardMember memberToUpdate = getMemberEntity(boardId, userId);

        if (memberToUpdate.getRole() == BoardRole.OWNER) {
            throw new InvalidRequestException("Cannot change role of the Owner");
        }

        memberToUpdate.setRole(newRole);
        memberRepository.save(memberToUpdate);
        return memberMapper.toResponse(memberToUpdate);
    }

    @Override
    @Transactional
    public void removeBoardMember(Long boardId, Long userIdToRemove) {
        permissionService.checkBoardAdminOrOwner(boardId);

        BoardMember memberToRemove = getMemberEntity(boardId, userIdToRemove);

        if (memberToRemove.getRole() == BoardRole.OWNER) {
            throw new InvalidRequestException("Cannot remove the Owner of the board");
        }

        Long currentUserId = securityUtils.getCurrentUser().getId();
        if (currentUserId.equals(userIdToRemove)) {
            throw new InvalidRequestException("You can't remove yourself");
        }

        memberRepository.delete(memberToRemove);
    }

    @Override
    @Transactional
    public void leaveBoard(Long boardId) {
        Long currentUserId = securityUtils.getCurrentUser().getId();

        BoardMember me = getMemberEntity(boardId, currentUserId);

        if (me.getRole() == BoardRole.OWNER) {
            throw new InvalidRequestException("Owner cannot leave the board. Please transfer ownership first.");
        }

        memberRepository.delete(me);
    }

    private BoardMember getMemberByBoardIdAndUserId(Long boardId, Long userId) {
        return getMemberEntity(boardId, userId);
    }

    private BoardMember getMemberEntity(Long boardId, Long userId) {
        return memberRepository.findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOARD_MEMBER_NOT_FOUND,"Member not found in board"));
    }
}
