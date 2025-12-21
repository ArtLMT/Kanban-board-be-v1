package com.lmt.Kanban.service;

import com.lmt.Kanban.dto.response.TaskResponse;
import com.lmt.Kanban.entity.Status;


import java.util.List;

public interface StatusService {
    void deleteStatus(Long statusId);
    void updateStatus(Long statusId, String statusName);
    void movePosition(Long statusId, Integer position);
    List<TaskResponse> getTasksByStatusId(Long statusId);
    Status getStatusEntity(Long statusId);
}
