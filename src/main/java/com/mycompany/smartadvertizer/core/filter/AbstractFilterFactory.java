/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import java.util.function.BiFunction;

/**
 *
 */
public abstract class AbstractFilterFactory<T> implements FilterFactory<T> {
    
    private final FilterFactoryRegistry filterFactoryRegistry;

    public AbstractFilterFactory(FilterFactoryRegistry filterFactoryRegistry) {
        this.filterFactoryRegistry = filterFactoryRegistry;        
    }

    protected FilterFactoryRegistry getFilterFactoryRegistry() {
        return filterFactoryRegistry;
    }    

    @Override
    public Filter<T> createFilter(String operator, T filterValue) {
        return new BaseFilter<>(getFunction(operator), filterValue);
    }
    
    protected abstract void register();

    protected abstract BiFunction<T, T, Boolean> getFunction(String operator);
}
