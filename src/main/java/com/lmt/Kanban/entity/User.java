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
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class User extends AbstractEntity {
    @NotBlank(message = "Username can't be empty")
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "Password can't be empty")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "display_name", nullable = true, length = 100)
    private String displayName;

    @ToString.Exclude // Khi gọi toString thì bỏ thằng này ra, nếu mà gọi luôn thằng này rồi trong thằng này lại gọi tiếp user thì toang, lặp vô tận rồi stack overflow luôn
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    // orphanRemoval = true: Nếu xóa User thì xóa luôn List Board của User đó đi
    private List<Board> boards = new ArrayList<>();
}
