package com.lmt.Kanban.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // Bắt buộc: Để JPA hiểu class này không tạo bảng riêng, mà các bảng con sẽ kế thừa cột của nó.
@SuperBuilder(toBuilder = true) // <--- QUAN TRỌNG: Thay cho @Builder
@NoArgsConstructor // <--- Bắt buộc cho JPA
@AllArgsConstructor // <--- Bắt buộc cho SuperBuilder
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp // Tự động lấy giờ hệ thống khi INSERT
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp // Tự động lấy giờ hệ thống khi UPDATE
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}