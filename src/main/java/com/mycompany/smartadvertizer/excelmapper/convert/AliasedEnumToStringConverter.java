/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.convert;

import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;

/**
 *
 */
public class AliasedEnumToStringConverter implements Converter<Enum, String>, ConditionalConverter {

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.isAssignableTo(new TypeDescriptor(ResolvableType.forClass(Aliased.class), null, null));
    }

    @Override
    public String convert(Enum source) {
        Aliased aliased = (Aliased) source;
        return aliased.getAlias();
    }
}
