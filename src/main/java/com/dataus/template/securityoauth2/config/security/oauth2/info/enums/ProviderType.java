package com.dataus.template.securityoauth2.config.security.oauth2.info.enums;

import java.util.Arrays;

import com.dataus.template.securityoauth2.common.exception.ErrorType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProviderType {

    GITHUB("01", "깃허브"),
    GOOGLE("02", "구글"),
    FACEBOOK("03", "페이스북");

    private final String code;
    private final String description;
    
    public static ProviderType ofCode(String code) {
        return Arrays.stream(ProviderType.values())        
                .filter(p -> p.getCode().equals(code))
                .findAny()
                .orElseThrow(() ->
                    ErrorType.INVALID_PROVIDER_CODE.getException());
    }
}
