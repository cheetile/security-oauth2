package com.dataus.template.securityoauth2.common.entity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter @Setter
public class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(name = "inst_id", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updt_id")
    private String lastModifiedBy;
    
}
