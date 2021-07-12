package com.dataus.template.securityoauth2.member.controller;

import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.dataus.template.securityoauth2.common.exception.ErrorType;
import com.dataus.template.securityoauth2.member.dto.MemberResponse;
import com.dataus.template.securityoauth2.member.dto.ModifyRequest;
import com.dataus.template.securityoauth2.member.entity.Member;
import com.dataus.template.securityoauth2.member.enums.RoleType;
import com.dataus.template.securityoauth2.member.principal.CurrentMember;
import com.dataus.template.securityoauth2.member.principal.OAuth2UserImpl;
import com.dataus.template.securityoauth2.member.service.MemberService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findMember(
        @PathVariable("id") Optional<Member> memberOptional) {
        
        return ResponseEntity.ok()
                .body(MemberResponse.of(
                    memberOptional.orElseThrow(() ->
                        ErrorType.UNAVAILABLE_PAGE.getException())));
    }

    
    @PatchMapping("/{id}")
    public ResponseEntity<?> modifyMember(
        @CurrentMember          OAuth2UserImpl principal,
        @PathVariable("id")     Long id,
        @Valid @RequestBody     ModifyRequest modifyRequest) {
        
        if(!principal.hasRole(id)) {
            throw ErrorType.MEMBER_NO_AUTHORITY.getException();
        }
        
        return ResponseEntity.ok()
                .body(memberService.modifyMember(id, modifyRequest));
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(
        @CurrentMember      OAuth2UserImpl principal,
        @PathVariable("id") Long id) {

        if(!principal.hasRole(id)) {
            throw ErrorType.MEMBER_NO_AUTHORITY.getException();
        }

        return ResponseEntity.ok()
                .body(memberService.deleteMember(id));        
    }

    @PutMapping("/{id}/changeRoles")
    public ResponseEntity<?> changeRoles(
        @CurrentMember      OAuth2UserImpl principal,
        @PathVariable("id") Long id,
        @NotEmpty @RequestBody Set<RoleType> roles) {
        
        if(!principal.getAuthorities().contains(RoleType.ROLE_ADMIN)) {
            throw ErrorType.MEMBER_NO_AUTHORITY.getException();
        }

        return ResponseEntity.ok()
                .body(memberService.changeRoles(id, roles));

    }
    
}
