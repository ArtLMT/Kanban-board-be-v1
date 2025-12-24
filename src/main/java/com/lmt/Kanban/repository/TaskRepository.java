package com.lmt.Kanban.repository;

import com.lmt.Kanban.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatusId(Long statusId);
    List<Task> findByBoardId(Long boardId);
    List<Task> findByAssigneeId(Long assigneeId);
    List<Task> findByBoardIdAndStatusId(Long boardId, Long statusId);
//    List<Task> findByCreatorId(Long creatorId);
}
