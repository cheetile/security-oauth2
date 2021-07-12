package com.dataus.template.securityoauth2.member.enums.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.dataus.template.securityoauth2.member.enums.RoleType;

@Converter
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

    @Override
    public String convertToDatabaseColumn(RoleType attribute) {
        return attribute.getCode();
    }

    @Override
    public RoleType convertToEntityAttribute(String dbData) {
        return RoleType.ofCode(dbData);
    }   
    
}
