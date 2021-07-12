package com.dataus.template.securityoauth2.member.dto;

import java.util.Collection;

import com.dataus.template.securityoauth2.config.security.oauth2.info.enums.ProviderType;
import com.dataus.template.securityoauth2.member.entity.Member;
import com.dataus.template.securityoauth2.member.principal.OAuth2UserImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
@JsonPropertyOrder({
    "id", "username", "provider",
    "name", "nickname", "email",
    "image", "roles"
})
public class MemberResponse {

    private Long id;

    private String username;

    private ProviderType provider;

    private String name;

    private String nickname;

    private String email;

    private String image;

    private Collection<? extends GrantedAuthority> roles; 

    @JsonInclude(Include.NON_NULL)
    private String jwt;

    public static MemberResponse of(Member member) {
        return MemberResponse.of(member, null);
    }

    public static MemberResponse of(Member member, String jwt) {
        return MemberResponse.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .provider(member.getProvider())
                    .name(member.getName())
                    .nickname(member.getNickname())
                    .email(member.getEmail())
                    .image(member.getImageUrl())
                    .roles(member.getRoleTypes())
                    .jwt(jwt)
                    .build();
    }

    public static MemberResponse of(OAuth2User principal) {
        return MemberResponse.of(principal, null);  
    }

    public static MemberResponse of(OAuth2User principal, String jwt) {
        
        OAuth2UserImpl user = (OAuth2UserImpl) principal;

        return MemberResponse.of(user.getMember(), jwt);
    }
    
}
