package com.lmt.Kanban.service;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.entity.Task;
import com.lmt.Kanban.entity.User;

public interface TaskPermissionService {
    void checkCanView(Task task);

    void checkCanCreate(Board board);

    void checkCanUpdate(Task task);

    void checkCanDelete(Task task);

    void checkCanAssign(Task task, User assignee);
}
