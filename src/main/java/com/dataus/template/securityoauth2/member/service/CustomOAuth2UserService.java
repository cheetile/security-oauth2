package com.dataus.template.securityoauth2.member.service;

import com.dataus.template.securityoauth2.config.security.oauth2.info.OAuth2UserInfo;
import com.dataus.template.securityoauth2.config.security.oauth2.info.OAuth2UserInfoFactory;
import com.dataus.template.securityoauth2.member.entity.Member;
import com.dataus.template.securityoauth2.member.entity.MemberRole;
import com.dataus.template.securityoauth2.member.enums.RoleType;
import com.dataus.template.securityoauth2.member.principal.OAuth2UserImpl;
import com.dataus.template.securityoauth2.member.repository.MemberRepository;
import com.dataus.template.securityoauth2.member.repository.MemberRoleRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    public OAuth2User loadUserByUsername(String username) {
        
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> 
                new UsernameNotFoundException("Member not Found"));
        
        return OAuth2UserImpl.of(member, null);
        
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2userRequest) throws OAuth2AuthenticationException {
        
        OAuth2User oAuth2User = super.loadUser(oAuth2userRequest);

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
            .getOAuth2UserInfo(
                oAuth2userRequest
                    .getClientRegistration()
                    .getRegistrationId(),
                oAuth2User.getAttributes());
        
        Member member = memberRepository.findByUsername(oAuth2UserInfo.getUsername())
            .orElseGet(() -> {
                Member m = memberRepository.save(
                    Member.of(oAuth2UserInfo));
                memberRoleRepository.save(new MemberRole(m, RoleType.ROLE_USER));
                
                return m;
            });

        return OAuth2UserImpl.of(member, oAuth2User.getAttributes());
    }
    
}
