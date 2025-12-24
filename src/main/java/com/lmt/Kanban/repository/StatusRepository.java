package com.lmt.Kanban.repository;

import com.lmt.Kanban.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findByBoardId(Long boardId);
    boolean existsByIdAndBoardId(Long id, Long BoardId);

    @Query("SELECT MAX(s.position) FROM Status s WHERE s.board.id = :boardId")
    Integer findMaxPositionByBoardId(@Param("boardId") Long boardId);


    // Case 1: Kéo từ Dưới lên Trên (VD: Từ 5 về 2)
    @Modifying
    @Query("UPDATE Status s SET s.position = s.position + 1 " +
            "WHERE s.board.id = :boardId AND s.position >= :newPos AND s.position < :oldPos")
    void shiftRight(@Param("boardId") Long boardId, @Param("newPos") Integer newPos, @Param("oldPos") Integer oldPos);

    // Case 2: Kéo từ Trên xuống Dưới (VD: Từ 2 xuống 5)
    @Modifying
    @Query("UPDATE Status s SET s.position = s.position - 1 " +
            "WHERE s.board.id = :boardId AND s.position > :oldPos AND s.position <= :newPos")
    void shiftLeft(@Param("boardId") Long boardId, @Param("oldPos") Integer oldPos, @Param("newPos") Integer newPos);
}
