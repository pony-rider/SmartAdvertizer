/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import com.mycompany.smartadvertizer.excelmapper.util.ReflectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.core.convert.ConversionService;

/**
 *
 */
public class FilterChain {

    private final List<PropertyFilter> filters;
    private final List<FilterOperator> operators;    
    
    public FilterChain(Class<?> type, FilterChainDescription description,
            FilterFactoryRegistry filterFactoryRegistry, ConversionService conversionService) {
        this.operators = description.operators;
        this.filters = new ArrayList<>();
        Map<String, Class<?>> propertyTypesMap = new HashMap<>();

        for (FilterDescriptor filterDescriptor : description.filterDescriptors) {
            String property = filterDescriptor.getProperty();
            String filterValue = filterDescriptor.getFilterValue();
            String operator = filterDescriptor.getOperator();
            Class<?> propertyType = propertyTypesMap.get(property);
            if (propertyType == null) {
                propertyType = ReflectionUtils.getPropertyType(type, property);
                propertyTypesMap.put(property, propertyType);
            }
            PropertyFilterFactory filterFactory = filterFactoryRegistry.
                    getPropertyFilterFactory(propertyType);            
            if (filterFactory == null) {
                throw new UnsupportedOperationException(
                        "no FilterFactory for type: " + propertyType);
            }       
            Object convertedFilterValue = conversionService.convert(filterValue, propertyType);   
            PropertyFilter filter = filterFactory.createFilter(operator, convertedFilterValue, property);
            filters.add(filter);
        }
    }

    private boolean recursiveEval(Iterator<FilterOperator> operatorIterator,
            Iterator<PropertyFilter> filterIterator, Object filterValue) {
        Filter filter = filterIterator.next();
        //System.out.println(((PropertyFilter) filter).getProperty());
        if (operatorIterator.hasNext()) {
            FilterOperator operator = operatorIterator.next();
            switch (operator) {
                case OR: {
                    return filter.accept(filterValue) || recursiveEval(operatorIterator,
                            filterIterator, filterValue);
                }
                case AND: {
                    //MAGIC: when use filterIterator.next without assignin value to filter2, 
                    //iterator return previos value instead of next value  
                    Filter filter2 = filterIterator.next();
                    boolean res = filter.accept(filterValue)
                            //&& filterIterator.next().accept(filterValue);
                            && filter2.accept(filterValue);
                    //System.out.println(((PropertyFilter)filter2).getProperty());
                    if (operatorIterator.hasNext()) {
                        operator = operatorIterator.next();
                        switch (operator) {
                            case OR: {
                                return res || recursiveEval(operatorIterator,
                                        filterIterator, filterValue);
                            }
                            case AND: {
                                return res && recursiveEval(operatorIterator,
                                        filterIterator, filterValue);
                            }
                            default: {
                                throw new UnsupportedOperationException(
                                        "Unsupported operator: " + operator);
                            }
                        }
                    } else {
                        return res;
                    }
                }
                default: {
                    throw new UnsupportedOperationException(
                            "Unsupported operator: " + operator);
                }
            }
        } else {
            return filter.accept(filterValue);
        }
    }

    public boolean accept(Object value) {
        Iterator<PropertyFilter> filterIterator = filters.iterator();
        Iterator<FilterOperator> operatorIterator = operators.iterator();
        return recursiveEval(operatorIterator, filterIterator, value);
    }
}
