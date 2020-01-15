/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.style;

/**
 *
 * 
 */
public class ColumnStyleInfo extends CellStyleInfo {

    private boolean autoWidth;
    private int width = -1;

    public ColumnStyleInfo() {
    }

    public ColumnStyleInfo(boolean border) {
        super(border);
    }

    public ColumnStyleInfo(boolean bordered, boolean wrapText, int width) {
        super(bordered, wrapText);
        setWidth(width);
    }

    public ColumnStyleInfo(boolean bordered, boolean wrapText, boolean autoWidth) {
        super(bordered, wrapText);
        this.autoWidth = autoWidth;
    }

    public ColumnStyleInfo setAutoWidth(boolean autoWidth) {
        this.autoWidth = autoWidth;
        this.width = -1;
        return this;
    }

    public ColumnStyleInfo setWidth(int width) {
        this.width = width * 256;
        this.autoWidth = false;
        return this;
    }

    public boolean getAutoWidth() {
        return autoWidth;
    }

    public int getWidth() {
        return width;
    }
}
