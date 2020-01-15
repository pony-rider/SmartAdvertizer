/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.convert;

import java.util.EnumSet;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 *
 */
public class StringToAliasedEnumConverterFactory implements ConverterFactory<String, Enum>, ConditionalConverter {

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return  new StringToAliasedEnumConverter(targetType);
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.isAssignableTo(new TypeDescriptor(ResolvableType.forClass(Aliased.class), null, null));
    }
    
    private static final class StringToAliasedEnumConverter<T extends Enum<T>> implements
            Converter<String, T> {

        private final Class<T> enumType;

        public StringToAliasedEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        public T convert(String source) {
            EnumSet<T> enumSet = EnumSet.allOf(enumType);

            return enumSet.stream().filter(e -> source.trim().equals(((Aliased) e).getAlias()))
                    .findAny().orElseThrow(() -> new IllegalArgumentException(
                                    "no enum value with alias: " + source));
        }
    }
}
