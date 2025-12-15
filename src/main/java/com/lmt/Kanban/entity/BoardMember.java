package com.lmt.Kanban.entity;

import jakarta.persistence.*;
import lombok.*;
import com.lmt.Kanban.common.enums.BoardRole;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name="board_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"board_id", "user_id"})
        }
)
@AttributeOverride(name = "id", column = @Column(name = "board_member_id"))
public class BoardMember extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @ToString.Exclude
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardRole role;
}
