package com.lmt.Kanban.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // Bắt buộc: Để JPA hiểu class này không tạo bảng riêng, mà các bảng con sẽ kế thừa cột của nó.
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
}