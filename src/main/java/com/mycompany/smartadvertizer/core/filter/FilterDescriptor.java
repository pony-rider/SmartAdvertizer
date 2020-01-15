/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

/**
 *
 */
public class FilterDescriptor {

    private final String property;
    private final String operator;
    private final String filterValue;

    public FilterDescriptor(String property, String operator, String filterValue) {
        this.property = property;
        this.operator = operator;
        this.filterValue = filterValue;
    }

    public String getProperty() {
        return property;
    }

    public String getOperator() {
        return operator;
    }

    public String getFilterValue() {
        return filterValue;
    }

    @Override
    public String toString() {
        return operator + " " + filterValue;
    }

    public FilterDescriptor setFilterValue(String filterValue) {
        FilterDescriptor descriptor = new FilterDescriptor(property, operator, filterValue);
        return descriptor;
    }
    
}
