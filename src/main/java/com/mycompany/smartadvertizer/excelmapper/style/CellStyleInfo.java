/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.style;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 *
 */
public class CellStyleInfo {

    private boolean bordered;
    private boolean wrapText;
    private HorizontalAlignment alignment = HorizontalAlignment.LEFT;
    private short color = -1;
    private short textRotation = 0;
    private String dateFormat;

    public CellStyleInfo() {
    }

    public CellStyleInfo(boolean border) {
        this.bordered = border;
    }

    public CellStyleInfo(boolean bordered, boolean wrapText) {
        this.bordered = bordered;
        this.wrapText = wrapText;
    }

    public CellStyleInfo setWrapText(boolean wrapText) {
        this.wrapText = wrapText;
        return this;
    }

    public CellStyleInfo setBordered(boolean bordered) {
        this.bordered = bordered;
        return this;
    }

    public CellStyleInfo setAlignment(HorizontalAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public CellStyleInfo setColor(short color) {
        this.color = color;
        return this;
    }

    public CellStyleInfo setTextRotation(short textRotation) {
        this.textRotation = textRotation;
        return this;
    }

    public boolean getWrapText() {
        return wrapText;
    }

    public boolean isBordered() {
        return bordered;
    }

    public HorizontalAlignment getAlignment() {
        return alignment;
    }

    public short getColor() {
        return color;
    }

    public boolean isColored() {
        return color != -1;
    }

    public short getTextRotation() {
        return textRotation;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public CellStyleInfo setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public CellStyle toCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        if (isBordered()) {
            addBorder(style);
        }
        style.setWrapText(getWrapText());
        if (getAlignment() != null) {
            style.setAlignment(getAlignment());
        }
        if (isColored()) {
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);            
            style.setFillForegroundColor(getColor());
        }
        style.setRotation(getTextRotation());
//        if (dateFormat != null && !dateFormat.isEmpty()) {
//            
//        }
        return style;
    }

    private void addBorder(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }
}
