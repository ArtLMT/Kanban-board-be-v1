package com.lmt.Kanban.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "status",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"board_id", "name"})
        }
)
@AttributeOverride(name = "id", column = @Column(name = "status_id"))
public class Status extends AbstractEntity{

    @NotBlank(message = "Status can't be empty")
    @Column(nullable = false)
    private String name;

    private String color;

    @Column(nullable = false)
    private Integer position;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ToString.Exclude
    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
}
