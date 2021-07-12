package com.dataus.template.securityoauth2.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter @Setter
public class BaseResponse {

    private boolean success;

    private String message;
    
}
