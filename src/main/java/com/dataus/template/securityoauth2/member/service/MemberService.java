package com.dataus.template.securityoauth2.member.service;

import java.util.Set;

import com.dataus.template.securityoauth2.common.dto.BaseResponse;
import com.dataus.template.securityoauth2.member.dto.ModifyRequest;
import com.dataus.template.securityoauth2.member.enums.RoleType;

public interface MemberService {

    BaseResponse modifyMember(Long id, ModifyRequest modifyRequest);

    BaseResponse deleteMember(Long id);

    BaseResponse changeRoles(Long id, Set<RoleType> roles);
    
}
