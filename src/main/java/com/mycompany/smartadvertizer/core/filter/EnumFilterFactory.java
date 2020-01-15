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
 * @author chelenver
 */
@Component
public class EnumFilterFactory extends AbstractFilterFactory<Enum> {

    @Autowired
    public EnumFilterFactory(FilterFactoryRegistry filterFactoryRegistry) {
        super(filterFactoryRegistry);
    }

    @PostConstruct
    @Override
    protected void register() {
        getFilterFactoryRegistry().addFilterFactory(Enum.class, this);
    }

    @Override
    protected BiFunction<Enum, Enum, Boolean> getFunction(String operator) {
        ObjectOperator objectOperator = ObjectOperator.getOperator(operator);
        switch (objectOperator) {
            case EQUALS: {
                return (value, filterValue) -> filterValue.equals(value);
            }
            case NOT_EQUALS: {
                return (value, filterValue) -> !filterValue.equals(value);
            }
            default: {
                throw new UnsupportedOperationException(
                        "unsupported operator: " + operator);
            }
        }
    }
}
