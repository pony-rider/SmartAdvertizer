/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

import com.mycompany.smartadvertizer.core.BillboardSide;
import com.mycompany.smartadvertizer.core.BillboardSide.Lighting;
import com.mycompany.smartadvertizer.core.config.AppConfig;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 *
 */
@SpringBootTest(classes = {AppConfig.class})
public class FilterChainTest {

    @Autowired
    private FilterFactoryRegistry filterFactoryRegistry;
    @Autowired
    private FilterChainFactory filterChainFactory;

    private FilterDescriptor sideFilter = new FilterDescriptor("side", "==", "А");
    private FilterDescriptor regionFilter = new FilterDescriptor("region", "!=",
            "Арбеково");
    private FilterDescriptor lightingFilter = new FilterDescriptor("lighting", "==",
            "+");
    private BillboardSide side1 = new BillboardSide("кулакова 7", "А");
    private BillboardSide side2 = new BillboardSide("кулакова 7", "В");
    private BillboardSide side3 = new BillboardSide("пр. строителей 5", "А");
    private BillboardSide side4 = new BillboardSide("пр. строителей 5", "В");
    private BillboardSide side5 = new BillboardSide("мира 11", "А");
    private BillboardSide side6 = new BillboardSide("мира 11", "В");

    {
        side1.setRegion("Центр");
        side2.setRegion("Центр");
        side3.setRegion("Арбеково");
        side4.setRegion("Арбеково");
        side5.setRegion("Южная поляна");
        side6.setRegion("Южная поляна");
        side1.setLighting(Lighting.ON);
        side2.setLighting(Lighting.OFF);
        side3.setLighting(Lighting.ON);
        side4.setLighting(Lighting.OFF);
        side5.setLighting(Lighting.OFF);
        side6.setLighting(Lighting.ON);
    }

    @Test
    public void testNestedAutowired() {
        FilterChainDescription description = new FilterChainDescription(
                sideFilter);
        FilterChain filterChain = filterChainFactory.createFilterChain(
                BillboardSide.class, description);
        assertTrue(filterChain.accept(side1));
    }

    @Test
    public void twoFiltersWithAndOperatorTest() {
        FilterChainDescription description = new FilterChainDescription(
                sideFilter).add(FilterOperator.AND, regionFilter);
        FilterChain filterChain = filterChainFactory.createFilterChain(
                BillboardSide.class, description);
        assertTrue(filterChain.accept(side1));
        assertFalse(filterChain.accept(side2));
        assertFalse(filterChain.accept(side3));
        assertFalse(filterChain.accept(side4));

    }

    @Test
    public void twoFiltersWithOrOperatorTest() {
        FilterChainDescription description = new FilterChainDescription(
                sideFilter).add(FilterOperator.OR, regionFilter);
        FilterChain filterChain = filterChainFactory.createFilterChain(
                BillboardSide.class, description);
        assertTrue(filterChain.accept(side1));
        assertTrue(filterChain.accept(side2));
        assertTrue(filterChain.accept(side3));
        assertFalse(filterChain.accept(side4));
    }

    @Test
    public void filtersWithCombinedOperatorsTest1() {
        FilterChainDescription description = new FilterChainDescription(sideFilter).add(
                FilterOperator.OR, regionFilter).add(FilterOperator.AND, lightingFilter);
        FilterChain filterChain = filterChainFactory.createFilterChain(
                BillboardSide.class, description);
        assertTrue(filterChain.accept(side1));
        assertFalse(filterChain.accept(side2));
        assertTrue(filterChain.accept(side3));
        assertFalse(filterChain.accept(side4));
        assertTrue(filterChain.accept(side5));
        assertTrue(filterChain.accept(side6));
    }

    @Test
    public void filtersWithCombinedOperatorsTest2() {
        FilterChainDescription description = new FilterChainDescription(sideFilter).add(
                FilterOperator.AND, regionFilter).add(FilterOperator.OR, lightingFilter);
        FilterChain filterChain = filterChainFactory.createFilterChain(
                BillboardSide.class, description);
        assertTrue(filterChain.accept(side1));
        assertFalse(filterChain.accept(side2));
        assertTrue(filterChain.accept(side3));
        assertFalse(filterChain.accept(side4));
        assertTrue(filterChain.accept(side5));
        assertTrue(filterChain.accept(side6));
    }

    @Test
    public void filtersWithCombinedOperatorsTest3() {
        FilterChainDescription description = new FilterChainDescription(sideFilter).add(
                FilterOperator.AND, regionFilter).add(FilterOperator.OR, lightingFilter);
        FilterChain filterChain = filterChainFactory.createFilterChain(
                BillboardSide.class, description);
        assertTrue(filterChain.accept(side1));
        assertFalse(filterChain.accept(side2));
        assertTrue(filterChain.accept(side3));
        assertFalse(filterChain.accept(side4));
        assertTrue(filterChain.accept(side5));
        assertTrue(filterChain.accept(side6));
    }
}
