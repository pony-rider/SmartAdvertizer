/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import java.util.EnumSet;

/**
 * 
 */
public enum NumberOperator {
    EQUALS("=="), NOT_EQUALS("!="), LOWER("<"), GREATER(">"),
    LOWER_OR_EQUALS("<="), GREATER_OR_EQUALS(">=");
    private final String symbol;

    private NumberOperator(String symbol) {
        this.symbol = symbol;
    }

    public static NumberOperator getOperator(String symbol) {
        EnumSet<NumberOperator> enumSet = EnumSet.allOf(NumberOperator.class);
        return enumSet.stream().filter(op -> symbol.equals(op.symbol))
                .findAny().orElseThrow(() -> new IllegalArgumentException(
                                "no operators with symbol: " + symbol));

    }
}
