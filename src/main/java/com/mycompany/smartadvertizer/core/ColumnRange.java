/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import java.util.Objects;

/**
 *
 */
public class ColumnRange {

    private final String startColumn;
    private final String endColumn;

    public ColumnRange(String startColumn, String endColumn) {
        this.startColumn = Objects.requireNonNull(startColumn);
        this.endColumn = Objects.requireNonNull(endColumn);
    }

    public String getStartColumn() {
        return startColumn;
    }

    public String getEndColumn() {
        return endColumn;
    }
}
