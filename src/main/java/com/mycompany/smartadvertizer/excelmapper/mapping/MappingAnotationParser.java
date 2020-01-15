/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.mapping;

import com.mycompany.smartadvertizer.excelmapper.mapping.annotation.ColumnStyle;
import com.mycompany.smartadvertizer.excelmapper.mapping.annotation.ColumnMapping;
import com.mycompany.smartadvertizer.excelmapper.mapping.annotation.InputColumn;
import com.mycompany.smartadvertizer.excelmapper.mapping.annotation.OutputColumn;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MappingAnotationParser {

    private static InputColumnMappingInfo createInputColumnMapping(String property,
            InputColumn column) {
        return new InputColumnMappingInfo(property, column.value(), column.nullable());
    }

    private static OutputColumnMappingInfo createOutputColumnMapping(String property,
            OutputColumn column) {
        ColumnStyle columnStyle = column.style();
        switch (column.excelType()) {
            case STRING: {
                return OutputColumnMappingInfo.createStringMapping(
                        property, column.header());
            }

            case NUMERIC: {
                return OutputColumnMappingInfo.createNumericMapping(property, column.
                        header());
            }

            case BOOLEAN: {
                return OutputColumnMappingInfo.createBooleanMapping(property, column.
                        header());
            }

            case DATE: {
                return OutputColumnMappingInfo.createDateMapping(
                        property, column.header(), column.dateFormat());
            }

            case HYPERLINK: {
                return OutputColumnMappingInfo.createHyperLinkMapping(property,
                        column.header(), column.hyperlinkText());
            }
            default: {
                throw new UnsupportedOperationException(
                        "unsupported excel type: " + column.excelType());
            }
        }
    }

    public static List<InputColumnMappingInfo> findInputColumnMappingInfo(Class<?> type) {
        List<InputColumnMappingInfo> inputColumnMappings = new ArrayList<>();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String property = field.getName();
            ColumnMapping excelMapping = field.getAnnotation(ColumnMapping.class);
            if (excelMapping != null) {
                inputColumnMappings.add(createInputColumnMapping(property,
                        excelMapping.input()));
            } else {
                InputColumn inputColumn = field.getAnnotation(InputColumn.class);
                if (inputColumn != null) {
                    inputColumnMappings.add(createInputColumnMapping(property,
                            excelMapping.input()));
                }
            }
        }
        return inputColumnMappings;
    }
    
    public static List<OutputColumnMappingInfo> findOutputColumnMappingInfo(Class<?> type) {
        List<OutputColumnMappingInfo> outputColumnMappings = new ArrayList<>();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String property = field.getName();
            ColumnMapping excelMapping = field.getAnnotation(ColumnMapping.class);
            if (excelMapping != null) {
                outputColumnMappings.add(createOutputColumnMapping(property,
                        excelMapping.output()));
            } else {
                OutputColumn outputColumn = field.getAnnotation(OutputColumn.class);
                if (outputColumn != null) {
                    outputColumnMappings.add(createOutputColumnMapping(property,
                            excelMapping.output()));
                }
            }
        }
        return outputColumnMappings;
    }
}
