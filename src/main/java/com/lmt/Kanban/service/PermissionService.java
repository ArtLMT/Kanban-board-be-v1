package com.lmt.Kanban.service;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.entity.Task;
import com.lmt.Kanban.entity.User;

public interface PermissionService {
    void checkBoardMember(Long boardId); // Chỉ cần là thành viên
    void checkBoardAdminOrOwner(Long boardId); // Phải là Admin hoặc Owner

    void checkTaskView(Task task);
    void checkTaskUpdate(Task task);
    void checkTaskDelete(Task task);
    void checkTaskAssign(Long boardId, Long assigneeId);
}
