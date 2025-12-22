package com.lmt.Kanban.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tasks")
@AttributeOverride(name = "id", column = @Column(name = "task_id"))
@SQLDelete(sql = "UPDATE tasks SET is_deleted = true WHERE task_id = ?")
@Where(clause = "is_deleted = false")
public class Task extends AbstractEntity {

    @NotBlank(message = "Title can't be empty") // NotBlamk kiểm tra đầu vào, chặn FE gửi
    @Column(nullable = false) // thằng này kiểm tra cấu trúc, chặn spl, be
    private String title;

    private String description;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // đứa tạo thì kh null đươc
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    // đứa nhận task thì có thể null
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id", nullable = true)
    private User assignee;
}