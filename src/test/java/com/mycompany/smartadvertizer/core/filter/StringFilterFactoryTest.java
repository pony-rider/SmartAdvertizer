/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

/**
 *
 * 
 */

public class StringFilterFactoryTest {    
    private FilterFactoryRegistry factoryRegistry = new FilterFactoryRegistryImpl();
    private FilterFactory<String> filterFactory = new StringFilterFactory(factoryRegistry);

    @Test
    public void testGetInvalidOperationStringFilter() {
        assertThrows(IllegalArgumentException.class,
                () -> filterFactory.createFilter(">", "abc"));
    }

    @Test
    public void testGetEqulasStringFilter() {
        Filter<String> equlasFilter = filterFactory.createFilter("==", "abc");
        assertTrue(equlasFilter.accept("abc"));
        assertFalse(equlasFilter.accept("ab"));
    }

    @Test
    public void testGetNotEqulasStringFilter() {
        Filter<String> notEqulasFilter = filterFactory.createFilter("!=", "abc");
        assertTrue(notEqulasFilter.accept("ab"));
        assertFalse(notEqulasFilter.accept("abc"));
    }

    @Test
    public void testContainsStringFilter() {
        Filter<String> containsFilter = filterFactory.createFilter("c", "ab");
        assertTrue(containsFilter.accept("abs"));
        assertTrue(containsFilter.accept("ab"));
        assertFalse(containsFilter.accept("acb"));
    }
}
