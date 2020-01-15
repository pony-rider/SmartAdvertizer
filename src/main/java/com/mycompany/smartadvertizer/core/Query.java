/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import com.mycompany.smartadvertizer.core.filter.FilterChainDescription;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class Query {

    private List<String> inputProperties;
    private List<String> outputProperties;
    private List<YearMonth> yearMonths;
    private FilterChainDescription filterChainDescription;

    private Query(List<String> inputProperties, List<String> outputProperties,
            List<YearMonth> yearMonths) {
        this.inputProperties = Objects.requireNonNull(inputProperties);
        this.outputProperties = Objects.requireNonNull(outputProperties);
        this.yearMonths = new ArrayList<>(Objects.requireNonNull(yearMonths));
        Collections.sort(this.yearMonths);
    }

    private Query(List<String> inputProperties, List<String> outputProperties,
            List<YearMonth> yearMonths,
            FilterChainDescription filterChainDescription) {
        this(inputProperties, outputProperties, yearMonths);
        this.filterChainDescription = Objects.requireNonNull(filterChainDescription);
    }

    public static Query from(List<String> inputProperties, List<String> outputProperties,
            YearMonth startMonth, YearMonth endMonth) {
        TimeUtil.checkPeriod(startMonth, endMonth);
        List<YearMonth> months = TimeUtil.getMonths(startMonth, endMonth);
        return new Query(inputProperties, outputProperties, months);
    }

    public static Query from(List<String> inputProperties, List<String> outputProperties,
            List<YearMonth> yearMonths) {
        TimeUtil.checkMonthRange(yearMonths);
        return new Query(inputProperties, outputProperties, yearMonths);
    }

    public static Query from(List<String> inputProperties, List<String> outputProperties,
            YearMonth startMonth, YearMonth endMonth, 
            FilterChainDescription filterChainDescription) {
        TimeUtil.checkPeriod(startMonth, endMonth);
        List<YearMonth> months = TimeUtil.getMonths(startMonth, endMonth);
        return new Query(inputProperties, outputProperties, months, filterChainDescription);
    }

    public static Query from(List<String> inputProperties, List<String> outputProperties,
            List<YearMonth> yearMonths, FilterChainDescription filterChainDescription) {
        TimeUtil.checkMonthRange(yearMonths);
        return new Query(inputProperties, outputProperties, yearMonths, filterChainDescription);
    }

    public List<String> getInputProperties() {
        return inputProperties;
    }

    public List<String> getOutputProperties() {
        return outputProperties;
    }

    public List<YearMonth> getYearMonths() {
        return yearMonths;
    }

    public FilterChainDescription getFilterChainDescription() {
        return filterChainDescription;
    }
}
