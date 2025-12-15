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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardMember> boardMemberships = new ArrayList<>();

}
