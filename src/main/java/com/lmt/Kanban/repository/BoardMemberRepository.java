package com.lmt.Kanban.repository;

import com.lmt.Kanban.entity.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
    List<BoardMember> findByUserIdAndRole(long userId, String role);
}
