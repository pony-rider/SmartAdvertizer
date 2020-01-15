/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * 
 */
public class TimeTest {

    @Disabled
    @Test
    public void test() {
        YearMonth startMonth = YearMonth.of(2019, Month.APRIL);
        YearMonth endMonth = YearMonth.of(2019, Month.MARCH);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy",
                new Locale("Ru", "ru"));
        String name = formatter.format(startMonth);
        System.out.println(formatter.format(startMonth));
        System.out.println(formatter.format(endMonth));
        System.out.println(startMonth.isAfter(endMonth));

    }
}
