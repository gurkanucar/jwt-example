package com.gucarsoft.jwtauth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    private boolean deleted;

    @JsonIgnore
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @CreationTimestamp
    private Timestamp created;

    @JsonIgnore
    @LastModifiedBy
    @Column(nullable = false)
    private String modifiedBy;

    @UpdateTimestamp
    private Timestamp modified;
}
