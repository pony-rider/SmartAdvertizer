/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.mapping;

import com.mycompany.smartadvertizer.excelmapper.style.ColumnStyleInfo;
import java.util.Objects;

/**
 *
 */
public class OutputColumnMappingInfo implements OutputColumnMapping {

    private final String property;
    private final String header;
    private final ExcelType excelType;
    private String hyperLinkText;
    private ColumnStyleInfo columnStyleInfo;
    private String dateFormat;

    private OutputColumnMappingInfo(String property, String header, ExcelType excelType) {
        this.property = Objects.requireNonNull(property, "property can't be null");
        this.header = Objects.requireNonNull(header, "header can't be null");
        this.excelType = Objects.requireNonNull(excelType, "excelType can't be null");
    }

    private OutputColumnMappingInfo setColumnStyleInfo(ColumnStyleInfo styleInfo) {
        this.columnStyleInfo = styleInfo;
        return this;
    }

    public static OutputColumnMappingInfo createStringMapping(String property,
            String header) {
        return new OutputColumnMappingInfo(property, header, ExcelType.STRING);
    }

    public static OutputColumnMappingInfo createStringMapping(String property,
            String header,
            ColumnStyleInfo columnStyleInfo) {
        return createStringMapping(property, header).setColumnStyleInfo(columnStyleInfo);
    }

    public static OutputColumnMappingInfo createNumericMapping(String property,
            String header) {
        return new OutputColumnMappingInfo(property, header, ExcelType.NUMERIC);
    }

    public static OutputColumnMappingInfo createNumericMapping(String property,
            String header,
            ColumnStyleInfo columnStyleInfo) {
        return createNumericMapping(property, header).setColumnStyleInfo(columnStyleInfo);
    }

    public static OutputColumnMappingInfo createBooleanMapping(String property,
            String header) {
        return new OutputColumnMappingInfo(property, header, ExcelType.BOOLEAN);
    }

    public static OutputColumnMappingInfo createBooleanMapping(String property,
            String header, ColumnStyleInfo columnStyleInfo) {
        return createBooleanMapping(property, header).setColumnStyleInfo(columnStyleInfo);
    }

    public static OutputColumnMappingInfo createDateMapping(String property, String header,
            String dateFormat) {
        OutputColumnMappingInfo mapping = new OutputColumnMappingInfo(property, header,
                ExcelType.DATE);
        mapping.dateFormat = Objects.requireNonNull(dateFormat,
                "dateFormat can't be null");
        return mapping;
    }

    public static OutputColumnMappingInfo createDateMapping(String property, String header,
            String dateFormat, ColumnStyleInfo columnStyleInfo) {
        return createDateMapping(property, header, dateFormat)
                .setColumnStyleInfo(columnStyleInfo);
    }

    public static OutputColumnMappingInfo createHyperLinkMapping(String property,
            String header, String hyperLinkText) {
        OutputColumnMappingInfo mapping = new OutputColumnMappingInfo(property, header,
                ExcelType.HYPERLINK);
        mapping.hyperLinkText = Objects.requireNonNull(hyperLinkText,
                "hyperLinkText can't be null");
        return mapping;
    }

    public static OutputColumnMapping createHyperLinkMapping(String property,
            String header, String hyperLinkText, ColumnStyleInfo columnStyleInfo) {
        return createHyperLinkMapping(property, header, hyperLinkText)
                .setColumnStyleInfo(columnStyleInfo);
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public ExcelType getExcelType() {
        return excelType;
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public String getHyperlinkText() {
        return hyperLinkText;
    }

    @Override
    public ColumnStyleInfo getColumnStyleInfo() {
        return columnStyleInfo;
    }

    @Override
    public String getDateFormat() {
        return dateFormat;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[property: ");
        sb.append(property);
        sb.append(", header: ");
        sb.append(header);
        sb.append(", excelType: ");
        sb.append(excelType);
        switch(excelType) {
            case DATE: {
                sb.append(", dateFormat: ");
                sb.append(dateFormat);
                break;
            }
            
            case HYPERLINK: {
                sb.append(", hyperLinkText: ");
                sb.append(hyperLinkText);
                break;
            }
        }
        return sb.toString();
    }

}
