/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import java.util.EnumSet;
import java.util.function.BiFunction;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class StringFilterFactory extends AbstractFilterFactory<String> {

    @Autowired
    public StringFilterFactory(FilterFactoryRegistry filterFactoryRegistry) {
        super(filterFactoryRegistry);
    }

    @Override
    protected BiFunction<String, String, Boolean> getFunction(String operator) {
        StringOperator stringOperator = StringOperator.getOperator(operator);
        switch (stringOperator) {
            case EQUALS: {
                return (value, filterValue) -> filterValue.equals(value);
            }
            case NOT_EQUALS: {
                return (value, filterValue) -> !filterValue.equals(value);
            }
            case CONTAINS: {
                return (value, filterValue) -> value != null
                        ? value.contains(filterValue) : false;
            }
            default: {
                throw new UnsupportedOperationException(
                        "unsupported operator: " + operator);
            }
        }
    }

    @PostConstruct
    @Override
    protected void register() {
        getFilterFactoryRegistry().addFilterFactory(String.class, this);
    }

    public static enum StringOperator {

        EQUALS("=="), NOT_EQUALS("!="), CONTAINS("c");
        private final String symbol;

        private StringOperator(String symbol) {
            this.symbol = symbol;
        }

        public static StringOperator getOperator(String symbol) {
            EnumSet<StringOperator> enumSet = EnumSet.allOf(StringOperator.class);
            return enumSet.stream().filter(op -> symbol.equals(op.symbol))
                    .findAny().orElseThrow(() -> new IllegalArgumentException(
                                    "no operators with symbol: " + symbol));

        }

    }
}
