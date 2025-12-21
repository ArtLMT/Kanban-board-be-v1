package com.lmt.Kanban.service.impl;

import com.lmt.Kanban.dto.response.TaskResponse;
import com.lmt.Kanban.entity.Status;
import com.lmt.Kanban.repository.BoardRepository;
import com.lmt.Kanban.repository.StatusRepository;
import com.lmt.Kanban.service.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final BoardRepository boardRepository;
    private final StatusRepository statusRepository;

    @Override
    public void deleteStatus(Long statusId) {

    }

    @Override
    public Status getStatusEntity (Long statusId) {

        return statusRepository.findById(statusId).orElse(null);
    }

    @Override
    public void updateStatus(Long statusId, String statusName) {

    }

    @Override
    public void movePosition(Long statusId, Integer position) {

    }

    @Override
    public List<TaskResponse> getTasksByStatusId(Long statusId) {
        return List.of();
    }
}
