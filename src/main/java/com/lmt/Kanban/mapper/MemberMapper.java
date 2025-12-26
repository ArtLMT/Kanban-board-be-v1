package com.lmt.Kanban.mapper;

import com.lmt.Kanban.dto.response.MemberResponse;
import com.lmt.Kanban.entity.BoardMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "createdAt", target = "joinTime")
    MemberResponse toResponse(BoardMember member);
}
