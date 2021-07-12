package com.dataus.template.securityoauth2.member.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dataus.template.securityoauth2.common.entity.BaseEntity;
import com.dataus.template.securityoauth2.member.enums.RoleType;
import com.dataus.template.securityoauth2.member.enums.converter.RoleTypeConverter;

import org.hibernate.annotations.Where;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_member_roles")
@Where(clause = "del_yn = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberRole extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Convert(converter = RoleTypeConverter.class)
    @Column(name = "role_cd", length = 2)
    private RoleType role; 

    public MemberRole(Member member, RoleType role) {
        this.member = member;
        this.role = role;

        member.getRoles().add(this);
    }
    
}
