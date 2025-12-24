package com.lmt.Kanban.service.impl;

import ch.qos.logback.core.status.StatusUtil;
import com.lmt.Kanban.dto.request.CreateStatusRequest;
import com.lmt.Kanban.dto.request.UpdateStatusRequest;
import com.lmt.Kanban.dto.response.StatusResponse;
import com.lmt.Kanban.dto.response.TaskResponse;
import com.lmt.Kanban.entity.Board;
import com.lmt.Kanban.entity.Status;
import com.lmt.Kanban.exception.ErrorCode;
import com.lmt.Kanban.exception.InvalidRequestException;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.mapper.StatusMapper;
import com.lmt.Kanban.repository.BoardRepository;
import com.lmt.Kanban.repository.StatusRepository;
import com.lmt.Kanban.service.BoardService;
import com.lmt.Kanban.service.StatusService;
import com.lmt.Kanban.service.TaskPermissionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final BoardRepository boardRepository;
    private final TaskPermissionService taskPermissionService;
    private final StatusRepository statusRepository;
    private final BoardService boardService;

    private final StatusMapper statusMapper;

    @Override
    public StatusResponse createStatus( Board board,CreateStatusRequest request) {
        Status status = statusMapper.toEntity(request);

        //TODO kiểm tra Permission

        status.setBoard(board);

        Integer maxPosition = statusRepository.findMaxPositionByBoardId(request.getBoardId());
        int currentMax = (maxPosition == null) ? 0 : maxPosition;

        status.setPosition(currentMax + 1);

        return statusMapper.toResponse(statusRepository.save(status));
    }

    @Override
    public StatusResponse updateStatus(UpdateStatusRequest request) {
        Status status = getStatusEntity(request.getId());

        statusMapper.updateStatusFromRequest(request, status);
        return createStatusResponse(status);
    }

    @Override
    @Transactional
    public void movePosition(Long statusId, Integer newPosition) {
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.STATUS_NOT_FOUND,"Status not found with ID: " + statusId));

        Long boardId = status.getBoard().getId();
        Integer oldPosition = status.getPosition();

        if (oldPosition.equals(newPosition)) {
            return;
        }

        if (newPosition < oldPosition) {
            statusRepository.shiftRight(boardId, newPosition, oldPosition);
        } else {
            statusRepository.shiftLeft(boardId, oldPosition, newPosition);
        }

        status.setPosition(newPosition);
        statusRepository.save(status);
    }

    @Override
    @Transactional
    public void deleteStatus(Long statusId) {
        Status status = getStatusEntity(statusId);
        Long boardId = status.getBoard().getId();
        Integer position = status.getPosition();

        statusRepository.delete(status);
//        statusRepository.shiftLeftAfterDelete(boardId, position);
    }

    @Override
    public Status validateStatusInBoard(Long statusId, Long boardId) {
        Status status = getStatusEntity(statusId);

        if (!status.getBoard().getId().equals(boardId)) {
            throw new InvalidRequestException("Status " + statusId + " does not belong to Board " + boardId);
        }
        return status;
    }

    @Override
    public StatusResponse getStatusById(Long statusId) {
        Status status = getStatusEntity(statusId);
        return createStatusResponse(status);
    }

    @Override
    public List<StatusResponse> getAllStatus(Long boardId) {
        if(!boardRepository.existsById(boardId)){
            throw new ResourceNotFoundException(ErrorCode.BOARD_NOT_FOUND,"Board not found with ID: " + boardId);
        }

        List<Status> statusList = statusRepository.findByBoardId(boardId);

        return statusList.stream()
                .map(this::createStatusResponse)
                .toList();
    }

    @Override
    public List<StatusResponse> getAllStatus() {
        List<Status> statusList = statusRepository.findAll();
        return statusList.stream()
                .map(this::createStatusResponse)
                .toList();
    }

    @Override
    public Boolean checkStatusInBoard(Long boardId, Long statusId) {
        return statusRepository.existsByIdAndBoardId(statusId, boardId);
    }

    private Status getStatusEntity (Long statusId) {
        System.out.println(statusId);
        return statusRepository.findById(statusId).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.STATUS_NOT_FOUND,"Status not found with ID: " + statusId));
    }

    private StatusResponse createStatusResponse(Status status) {
        return StatusResponse.builder()
                .id(status.getId())
                .color(status.getColor())
                .name(status.getName())
                .position(status.getPosition())
                .boardId(status.getBoard().getId())
                .build();
    }

}
