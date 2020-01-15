/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import java.util.function.BiFunction;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class DoubleFilterFactory extends AbstractFilterFactory<Double> {

    @Autowired
    public DoubleFilterFactory(FilterFactoryRegistry filterFactoryRegistry) {
        super(filterFactoryRegistry);
    }

    @PostConstruct
    @Override
    protected void register() {
        getFilterFactoryRegistry().addFilterFactory(Double.class, this);
    }

    @Override
    protected BiFunction<Double, Double, Boolean> getFunction(String operator) {
        NumberOperator numberOperator = NumberOperator.getOperator(operator);
        switch (numberOperator) {
            case EQUALS: {
                return (value, filterValue) -> filterValue.equals(value);
            }
            case NOT_EQUALS: {
                return (value, filterValue) -> !filterValue.equals(value);
            }
            case GREATER: {
                return (value, filterValue) -> value != null ? value > filterValue : false;
            }

            case LOWER: {
                return (value, filterValue) -> value != null ? value < filterValue : false;
            }

            case GREATER_OR_EQUALS: {
                return (value, filterValue) -> value != null ? value >= filterValue : false;
            }

            case LOWER_OR_EQUALS: {
                return (value, filterValue) -> value != null ? value <= filterValue : false;
            }

            default: {
                throw new UnsupportedOperationException(
                        "unsupported operator: " + operator);
            }
        }
    }

}
