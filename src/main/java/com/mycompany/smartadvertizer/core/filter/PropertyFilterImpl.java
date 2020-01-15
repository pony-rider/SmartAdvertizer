/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import com.mycompany.smartadvertizer.excelmapper.util.ReflectionUtils;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * 
 */
public class PropertyFilterImpl<T> implements PropertyFilter<T> {

    private final Filter<T> filter;
    private final String property;

    public PropertyFilterImpl(Filter<T> filter, String property) {
        this.filter = Objects.requireNonNull(filter, "filter can't be null");
        this.property = Objects.requireNonNull(property,"property can't be null");
    }

    @Override
    public boolean accept(T obj) {        
        T value = (T) ReflectionUtils.getProperty(obj, property);        
        return filter.accept(value);
    }

    @Override
    public String getProperty() {
        return property;
    }
}
