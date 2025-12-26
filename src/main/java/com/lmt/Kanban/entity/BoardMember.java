package com.lmt.Kanban.entity;

import jakarta.persistence.*;
import lombok.*;
import com.lmt.Kanban.common.enums.BoardRole;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(
        name="board_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"board_id", "user_id"})
        }
)
@AttributeOverride(name = "id", column = @Column(name = "board_member_id"))
@SQLDelete(sql = "UPDATE board_members SET is_deleted = true WHERE board_member_id = ?")
@Where(clause = "is_deleted = false")
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
