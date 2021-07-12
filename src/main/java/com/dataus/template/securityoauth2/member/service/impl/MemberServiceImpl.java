package com.dataus.template.securityoauth2.member.service.impl;

import java.util.Set;

import javax.transaction.Transactional;

import com.dataus.template.securityoauth2.common.dto.BaseResponse;
import com.dataus.template.securityoauth2.common.exception.ErrorType;
import com.dataus.template.securityoauth2.member.dto.ModifyRequest;
import com.dataus.template.securityoauth2.member.entity.Member;
import com.dataus.template.securityoauth2.member.entity.MemberRole;
import com.dataus.template.securityoauth2.member.enums.RoleType;
import com.dataus.template.securityoauth2.member.repository.MemberRepository;
import com.dataus.template.securityoauth2.member.repository.MemberRoleRepository;
import com.dataus.template.securityoauth2.member.service.MemberService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    @Override
    public BaseResponse modifyMember(Long id, ModifyRequest modifyRequest) {
        Member member = memberRepository
            .findById(id)
            .orElseThrow(() ->
                ErrorType.NO_MEMBER_ID.getException());

        member.modify(modifyRequest);        
        
        return BaseResponse.builder()
                    .success(true)
                    .message(String.format(
                        "Modified Member Id[%s]", 
                        member.getUsername()))
                    .build();
    }

    @Override
    public BaseResponse deleteMember(Long id) {
        Member member = memberRepository
            .findById(id)
            .orElseThrow(() ->
                ErrorType.NO_MEMBER_ID.getException());

        member.delete();
           
        return BaseResponse.builder()
                    .success(true)
                    .message(String.format(
                        "Deleted Member Id[%s]", 
                        member.getUsername()))
                    .build();
    }

    @Override
    public BaseResponse changeRoles(Long id, Set<RoleType> roles) {
        Member member = memberRepository
            .findById(id)
            .orElseThrow(() ->
                ErrorType.NO_MEMBER_ID.getException());
        
        member.deleteRoles();
        roles.forEach(r -> 
            memberRoleRepository.save(new MemberRole(member, r)));
        
        return BaseResponse.builder()
            .success(true)
            .message(String.format(
                "Roles of member Id[%s] set to %s", 
                member.getUsername(),
                member.getRoleTypes()))
            .build();
    }

}
