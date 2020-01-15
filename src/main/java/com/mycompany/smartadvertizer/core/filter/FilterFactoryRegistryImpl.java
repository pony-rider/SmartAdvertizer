/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 */

@Component
public class FilterFactoryRegistryImpl implements FilterFactoryRegistry{
    private Map<Class<?>, FilterFactory<?>> filterFactoryMap = new HashMap<>();

    @Override
    public void addFilterFactory(Class<?> fieldType, FilterFactory<?> filterFactory) {
        filterFactoryMap.put(fieldType, filterFactory);
    }


    @Override
    public FilterFactory<?> getFilterFactory(Class<?> fieldType) {
        if (fieldType.isEnum()) {            
            return filterFactoryMap.get(Enum.class);
        }
        return filterFactoryMap.get(fieldType);
    }

    @Override
    public PropertyFilterFactory<?> getPropertyFilterFactory(Class<?> fieldType) {     
        FilterFactory filterFactory = getFilterFactory(fieldType);
        if (filterFactory == null) {
            return null;
        }        
        return new PropertyFilterFactoryImpl<>(getFilterFactory(fieldType));
    }
}
