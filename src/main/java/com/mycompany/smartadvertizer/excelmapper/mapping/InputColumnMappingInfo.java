/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.mapping;

import org.apache.poi.ss.util.CellReference;

/**
 *
 * @author user
 */
public class InputColumnMappingInfo implements InputColumnMapping {

    private String property;
    private int columnIndex;
    private String literalColumnIndex;
    private boolean nullable = true;

    public InputColumnMappingInfo(String property, String literalColumnIndex) {
        this.property = property;
        this.literalColumnIndex = literalColumnIndex;
        setIndex(literalColumnIndex);
    }

    public InputColumnMappingInfo(String property, String literalColumnIndex,
            boolean nullable) {
        this(property, literalColumnIndex);
        this.nullable = nullable;
    }

    private void setIndex(String literalColumnIndex) {
        this.columnIndex = CellReference.convertColStringToIndex(literalColumnIndex);
        if (columnIndex < 0) {
            throw new IllegalArgumentException("invalid literal column index: "
                    + literalColumnIndex);
        }
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
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public String toString() {
        return String.format("[property: %s, column: %s, nullable: %b]", property,
                literalColumnIndex, nullable);
    }
}
