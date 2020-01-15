/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.mapping;

import com.mycompany.smartadvertizer.core.BillboardSide;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 */
@Disabled
public class MapingAnnotationTest {

    private Class<?> type = BillboardSide.class;

    @Test
    public void findInputColumnMappingInfoTest() {
        List<InputColumnMappingInfo> columnMappings = MappingAnotationParser.
                findInputColumnMappingInfo(type);
        System.out.println("\nInputMappings");
        columnMappings.forEach(System.out::println);
    }

    @Test
    public void findOutputColumnMappingInfoTest() {
        List<OutputColumnMappingInfo> columnMappings = MappingAnotationParser.
                findOutputColumnMappingInfo(type);
        System.out.println("\nOutputMappings");
        columnMappings.forEach(System.out::println);
    }

}
