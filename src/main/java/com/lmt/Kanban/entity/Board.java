package com.lmt.Kanban.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boards")
@Builder@AttributeOverride(name = "id", column = @Column(name = "board_id"))
public class Board extends AbstractEntity {

    @NotBlank(message = "Title can't be empty")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
}
