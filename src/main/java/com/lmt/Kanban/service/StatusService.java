package com.lmt.Kanban.service;

import com.lmt.Kanban.dto.request.CreateStatusRequest;
import com.lmt.Kanban.dto.request.UpdateStatusRequest;
import com.lmt.Kanban.dto.response.StatusResponse;
import com.lmt.Kanban.dto.response.TaskResponse;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.entity.Status;


import java.util.List;

public interface StatusService {
    StatusResponse createStatus(Board board, CreateStatusRequest request);
    void deleteStatus(Long statusId);
    StatusResponse updateStatus(UpdateStatusRequest request);
    void movePosition(Long statusId, Integer position);
    Status validateStatusInBoard(Long statusId, Long boardId);
    StatusResponse getStatusById(Long statusId);
    Boolean checkStatusInBoard(Long boardId, Long statusId);
    List<StatusResponse> getAllStatus();
    List<StatusResponse> getAllStatus(Long boardId);
}
