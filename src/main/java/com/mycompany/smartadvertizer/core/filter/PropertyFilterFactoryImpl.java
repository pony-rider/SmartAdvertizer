/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import java.util.Objects;

/**
 *
 */
public class PropertyFilterFactoryImpl<T> implements PropertyFilterFactory<T>{
    private final FilterFactory<T> filterFactory;

    public PropertyFilterFactoryImpl(FilterFactory<T> filterFactory) {
        this.filterFactory = Objects.requireNonNull(filterFactory, "filter factory can't be null");
    }    

    @Override
    public PropertyFilter<T> createFilter(String operator, T filterValue, String property) {
        return new PropertyFilterImpl<>(filterFactory.createFilter(operator, filterValue), property);
    }    
}
