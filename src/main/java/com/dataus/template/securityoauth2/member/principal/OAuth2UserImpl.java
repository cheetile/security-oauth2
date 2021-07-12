package com.dataus.template.securityoauth2.member.principal;

import java.util.Collection;
import java.util.Map;

import com.dataus.template.securityoauth2.member.entity.Member;
import com.dataus.template.securityoauth2.member.enums.RoleType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2UserImpl implements OAuth2User {

    private String name;

    private Collection<? extends GrantedAuthority> authorities;
    
    private Map<String, Object> attributes;

    private Member member;


    public OAuth2UserImpl(Member member, Map<String, Object> attributes) {
        this.name = member.getUsername();
        this.authorities = member.getRoleTypes();
        this.attributes = attributes;
        this.member = member;
    }    

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    } 

    public Member getMember() {
        return member;
    }

    public static OAuth2User of(Member member, Map<String, Object> attributes) {
        return new OAuth2UserImpl(member, attributes);
    }

    public boolean hasRole(Long id) {
        return this.member.getId().longValue() == id.longValue() ||
               this.authorities.contains(RoleType.ROLE_ADMIN);
    }
    
}
