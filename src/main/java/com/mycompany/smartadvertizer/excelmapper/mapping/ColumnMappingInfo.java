/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.mapping;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mycompany.smartadvertizer.excelmapper.style.ColumnStyleInfo;
import java.util.Objects;
import org.apache.poi.ss.util.CellReference;

/**
 *
 */
public class ColumnMappingInfo implements InputColumnMapping, OutputColumnMapping {

    private String property;
    private int columnIndex;
    private String literalColumnIndex;
    private boolean nullable = true;
    private String header;
    private ExcelType excelType;
    private String hyperlinkText;
    private ColumnStyleInfo columnStyleInfo;
    private String dateFormat = "dd.MM.yy";

    private static final ExcelType defaultType = ExcelType.STRING;

    @JsonCreator
    public ColumnMappingInfo(String property, String column, String header) {
        this.property = Objects.requireNonNull(property);
        setIndex(Objects.requireNonNull(column));
        this.header = Objects.requireNonNull(header);
        this.excelType = defaultType;

    }

    public ColumnMappingInfo(String property, String column, String header,
            ExcelType excelType) {
        this(property, column, header);
        this.excelType = Objects.requireNonNull(excelType);
    }

    private void setIndex(String literalColumnIndex) {
        this.columnIndex = CellReference.convertColStringToIndex(literalColumnIndex);
        if (columnIndex < 0) {
            throw new IllegalArgumentException("invalid literal column index: "
                    + literalColumnIndex);
        }
    }

    public static ColumnMappingInfo hyperlinkMapping(String property,
            String literalColumnIndex, String header, String hyperlinkText) {
        ColumnMappingInfo columnMapping = new ColumnMappingInfo(property,
                literalColumnIndex,
                header, ExcelType.HYPERLINK);
        columnMapping.setHyperlinkText(hyperlinkText);
        return columnMapping;
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public int getColumnIndex() {
        return columnIndex;
    }

    public String getLiteralColumnIndex() {
        return literalColumnIndex;
    }

    @Override
    public ExcelType getExcelType() {
        return excelType;
    }

    @Override
    public String getHeader() {
        return header;
    }

    public void setHyperlinkText(String hyperlinkText) {
        this.hyperlinkText = hyperlinkText;
    }

    @Override
    public String getHyperlinkText() {
        return hyperlinkText;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    public ColumnMappingInfo setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    @Override
    public ColumnStyleInfo getColumnStyleInfo() {
        return columnStyleInfo;
    }

    public ColumnMappingInfo setColumnStyleInfo(ColumnStyleInfo columnStyleInfo) {
        this.columnStyleInfo = columnStyleInfo;
        return this;
    }

    @Override
    public String getDateFormat() {
        return dateFormat;
    }

    @Override
    public String toString() {
        return String.format("property: %s, column: %d", getProperty(), getColumnIndex());
    }

}
