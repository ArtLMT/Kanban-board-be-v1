package com.lmt.Kanban.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boards")
@Builder@AttributeOverride(name = "id", column = @Column(name = "board_id"))
@SQLDelete(sql = "UPDATE boards SET is_deleted = true WHERE board_id = ?")
@Where(clause = "is_deleted = false")
public class Board extends AbstractEntity {

    @NotBlank(message = "Title can't be empty")
    private String title;

    @ToString.Exclude // Khi gọi toString thì bỏ thằng này ra, nếu mà gọi luôn thằng này rồi trong thằng này lại gọi tiếp status thì toang, lặp vô tận rồi stack overflow luôn
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Status> statuses = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardMember> members = new ArrayList<>();
}
