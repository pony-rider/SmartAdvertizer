/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class FilterChainFactory {

    @Autowired
    private FilterFactoryRegistry filterFactoryRegistry;
    @Autowired
    private ConversionService conversionService;

    public FilterChain createFilterChain(Class<?> type, FilterChainDescription description) {
        return new FilterChain(type, description, filterFactoryRegistry, conversionService);
    }
}
