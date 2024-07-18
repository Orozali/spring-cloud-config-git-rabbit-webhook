package com.easyBytes.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseEntity {

    @Column(updatable = false)
    @CreatedDate
    LocalDateTime createdAt;

    @Column(updatable = false)
    @CreatedBy
    String createdBy;

    @Column(insertable = false)
    @LastModifiedDate
    LocalDateTime updatedAt;

    @Column(insertable = false)
    @LastModifiedBy
    String updatedBy;

}
