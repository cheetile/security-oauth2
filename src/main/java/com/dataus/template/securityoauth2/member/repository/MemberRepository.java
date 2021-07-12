package com.dataus.template.securityoauth2.member.repository;

import java.util.Optional;

import com.dataus.template.securityoauth2.member.entity.Member;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    @EntityGraph("Member.role")
    Optional<Member> findByUsername(String username);

    @EntityGraph("Member.role")
    Optional<Member> findById(Long id);

}
