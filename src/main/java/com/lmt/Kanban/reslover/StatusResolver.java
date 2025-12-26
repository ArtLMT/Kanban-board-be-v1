package com.lmt.Kanban.reslover;

import com.lmt.Kanban.entity.Status;
import com.lmt.Kanban.exception.ErrorCode;
import com.lmt.Kanban.exception.ResourceNotFoundException;
import com.lmt.Kanban.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatusResolver {

    private final StatusRepository statusRepository;

    public Status findByIdOrThrow(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.STATUS_NOT_FOUND,
                        "Status not found: " + id
                ));
    }
}

