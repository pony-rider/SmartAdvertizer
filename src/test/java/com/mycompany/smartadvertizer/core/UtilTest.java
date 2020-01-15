/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import com.mycompany.smartadvertizer.excelmapper.util.Util;
import com.mycompany.smartadvertizer.excelmapper.mapping.ColumnMappingInfo;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * 
 */
public class UtilTest {

    @Disabled
    @Test
    public void toMap() {
        List<ColumnMappingInfo> columnMappings = Arrays.asList(new ColumnMappingInfo("addrres", "A", "Адрес"),
                new ColumnMappingInfo("side", "B", "Сторона")
        );
        Map<String, ColumnMappingInfo> map = Util.toMap(columnMappings,
                ColumnMappingInfo::getProperty);
        System.out.println(map);
    }
}
