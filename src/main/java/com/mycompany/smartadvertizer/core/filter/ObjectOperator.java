/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import java.util.EnumSet;

/**
 *
 * @author chelenver
 */
public enum ObjectOperator {
    EQUALS("=="), NOT_EQUALS("!=");
    private final String symbol;

    private ObjectOperator(String symbol) {
        this.symbol = symbol;
    }

    public static ObjectOperator getOperator(String symbol) {
        EnumSet<ObjectOperator> enumSet = EnumSet.allOf(ObjectOperator.class);
        return enumSet.stream().filter(op -> symbol.equals(op.symbol))
                .findAny().orElseThrow(() -> new IllegalArgumentException(
                "no operators with symbol: " + symbol));

    }
}
