package com.lmt.Kanban.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The actual token value (usually a long, secure UUID-like string)
    @Column(nullable = false, unique = true)
    private String token;

    // Links the token to a specific user
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    // Defines when the token will expire (security measure)
    @Column(nullable = false)
    private Instant expiryDate;

    // To check if its used?
    @Column(nullable = false)
    private boolean revoked = false;
}
