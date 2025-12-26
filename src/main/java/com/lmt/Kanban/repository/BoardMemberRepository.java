package com.lmt.Kanban.repository;

import com.lmt.Kanban.common.enums.BoardRole;
import com.lmt.Kanban.entity.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
    List<BoardMember> findByUserIdAndRole(Long userId, BoardRole role);
    List<BoardMember> findByBoardId(Long boardId);
    boolean existsByUserIdAndBoardId(Long userId, Long boardId);
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
    Optional<BoardMember> findByBoardIdAndUserId(Long boardId, Long userId);

    @Query("""
    select bm.role
    from BoardMember bm
    where bm.board.id = :boardId
      and bm.user.id = :userId
    """)
    Optional<BoardRole> findRoleByBoardIdAndUserId(    @Param("boardId") Long boardId,
                                                       @Param("userId") Long userId
    );

    @Query(value = "SELECT * FROM board_members WHERE board_id = :boardId AND user_id = :userId", nativeQuery = true)
    Optional<BoardMember> findMemberIncludeDeleted(@Param("boardId") Long boardId, @Param("userId") Long userId);
}
