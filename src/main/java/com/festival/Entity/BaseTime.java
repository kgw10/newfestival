package com.festival.Entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value={AuditingEntityListener.class})
@MappedSuperclass
@Data
public abstract class BaseTime {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;
    @LastModifiedDate
    private LocalDateTime updateTime;
}
