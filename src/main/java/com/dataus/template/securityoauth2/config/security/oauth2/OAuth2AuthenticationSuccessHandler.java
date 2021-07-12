package com.dataus.template.securityoauth2.config.security.oauth2;

import static com.dataus.template.securityoauth2.config.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataus.template.securityoauth2.common.exception.ErrorType;
import com.dataus.template.securityoauth2.common.property.AppProperties;
import com.dataus.template.securityoauth2.common.utils.CookieUtils;
import com.dataus.template.securityoauth2.common.utils.JwtUtils;
import com.dataus.template.securityoauth2.member.dto.MemberResponse;
import com.dataus.template.securityoauth2.member.principal.OAuth2UserImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final JwtUtils jwtUtils;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final AppProperties appProperties;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, 
        HttpServletResponse response, 
        Authentication authentication
    ) throws IOException, ServletException {

        String targetUrl = determineTargetUrl(request);

        OAuth2UserImpl principal = (OAuth2UserImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(principal);

        String json = new ObjectMapper()
            .writeValueAsString(
                MemberResponse.of(principal.getMember(), jwt));
        
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.getWriter().write(json);
        response.flushBuffer();
        
        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository
            .removeAuthorizationRequestCookies(request, response);
        
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        
    }

    protected String determineTargetUrl(HttpServletRequest request) {

        Optional<String> redirectUri = CookieUtils
            .getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map(Cookie::getValue);
            
        redirectUri.ifPresent(uri -> {
            if(!isAuthorizedRedirectUri(uri))
                throw ErrorType.UNAUTHORIZED_REDIRECTION.getException();
        });

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        
        return UriComponentsBuilder
                .fromUriString(targetUrl)
                .build()
                .toUriString();
    }
    
    private boolean isAuthorizedRedirectUri(String uri) {
        
        URI clientRedirectUri = URI.create(uri);
        
        return appProperties.getOAuth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
    
}