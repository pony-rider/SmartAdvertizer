/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 *
 *
 */
public class TimeUtil {

    public static void checkMonthRange(List<YearMonth> months) {
        Set<YearMonth> monthsSet = new LinkedHashSet<>(months);
        if (months.size() != monthsSet.size()) {
            throw new IllegalArgumentException("duplicated monthes: "
                    + months.removeAll(monthsSet));
        }

    }

    public static void checkPeriod(YearMonth startMonth, YearMonth endMonth) {
        if (startMonth.isAfter(endMonth)) {
            throw new IllegalArgumentException("startMonth is after endMonth"
                    + startMonth + " " + endMonth);
        }
    }

    public static List<YearMonth> getMonths(YearMonth start, YearMonth end) {
        List<YearMonth> months = new ArrayList<>();
        for (YearMonth temp = start; temp.compareTo(end) < 1; temp = temp.plusMonths(1)) {
            months.add(temp);
        }

        return months;
    }

    public static List<String> getMonthsNames(YearMonth start, YearMonth end) {
        List<YearMonth> months = TimeUtil.getMonths(start, end);
        return getMonthsNames(months);
    }

    public static List<String> getMonthsNames(List<YearMonth> months) {
        List<String> names = new ArrayList<>();
        for (YearMonth yearMonth : months) {
            String name = yearMonth.getMonth().getDisplayName(TextStyle.FULL_STANDALONE,
                    new Locale("Ru", "ru"));
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy",
//                    new Locale("Ru", "ru"));
//            String name = formatter.format(month);
            names.add(name);
        }
        return names;
    }
}
