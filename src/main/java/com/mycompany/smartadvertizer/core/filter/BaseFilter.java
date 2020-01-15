/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @param <T>
 */
public class BaseFilter<T> implements Filter<T> {

    private final BiFunction<T, T, Boolean> operator;
    private final T filterValue;

    public BaseFilter(BiFunction<T, T, Boolean> operator, T filterValue) {
        this.operator = Objects.requireNonNull(operator, "operator can't be null");
        this.filterValue = Objects.requireNonNull(filterValue, "filter value can't be null");
    }

    @Override
    public boolean accept(T value) {        
        return operator.apply(value, filterValue);
    }
}
