package com.mycompany.smartadvertizer.core.config;

import com.mycompany.smartadvertizer.core.*;
import com.mycompany.smartadvertizer.excelmapper.convert.AliasedEnumToStringConverter;
import com.mycompany.smartadvertizer.excelmapper.convert.LocalDateToDateConverter;
import com.mycompany.smartadvertizer.excelmapper.convert.StringToAliasedEnumConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 *
 */
@Configuration
@ComponentScan({"com.mycompany.smartadvertizer.core", 
    "com.mycompany.smartadvertizer.excelmapper"})
public class AppConfig {
    @Bean
    public ConversionService getConversionService() {
        ConfigurableConversionService service = new DefaultFormattingConversionService();
        service.addConverterFactory(new StringToAliasedEnumConverterFactory());
        service.addConverter(new AliasedEnumToStringConverter());
        service.addConverter(new LocalDateToDateConverter());
        return service;
    }

    @Bean
    public Config getConfig() {
        return new Config();
    }
}
