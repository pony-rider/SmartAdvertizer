/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 
 */
public class FilterChainDescription {

    List<FilterDescriptor> filterDescriptors;
    List<FilterOperator> operators;

    public FilterChainDescription(List<FilterDescriptor> filterDescriptors,
            List<FilterOperator> operators) {
        this.filterDescriptors = Objects.requireNonNull(filterDescriptors);
        this.operators = Objects.requireNonNull(operators);
        if (filterDescriptors.size() - 1 != operators.size()) {
            throw new IllegalArgumentException(String.format(
                    "The number of filters must be less by 1 than operators: "
                    + "filters %d, opearators %d", filterDescriptors.size(),
                    operators.size()));
        }
    }

    public FilterChainDescription(FilterDescriptor filter) {
        filterDescriptors = new ArrayList<>();
        filterDescriptors.add(filter);
        operators = new ArrayList<>();
    }

    public FilterChainDescription add(FilterOperator op, FilterDescriptor filter) {
        operators.add(op);
        filterDescriptors.add(filter);
        return this;
    }

    public static FilterChainDescription createSingleTypeFilterChainDescription(
            String property, String operator, List<String> values,
            FilterOperator filterOperator) {
        Objects.requireNonNull(values);
        Iterator<String> filterValueIterator = values.iterator();
        FilterDescriptor filterDescriptor = null;
        if (filterValueIterator.hasNext()) {
            filterDescriptor = new FilterDescriptor(property, operator, filterValueIterator.
                    next());
        } else {
            throw new IllegalArgumentException("list of filter values can't be empty");
        }
        FilterChainDescription filterChainDescription = new FilterChainDescription(
                filterDescriptor);
        while (filterValueIterator.hasNext()) {
            String filterValue = filterValueIterator.next();
            filterDescriptor = filterDescriptor.setFilterValue(filterValue);
            filterChainDescription.add(filterOperator, filterDescriptor);
        }
        return filterChainDescription;
    }
}
