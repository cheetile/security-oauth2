package com.dataus.template.securityoauth2.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModifyRequest {

    @Size(max = 100) @NotBlank
    private String name;

    @Size(max = 100) @NotBlank
    private String nickname;

    @Email @Size(max = 320)
    private String email;

    @Size(max = 2083)
    private String image;
    
}
