/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.mapping;

import com.mycompany.smartadvertizer.excelmapper.style.ColumnStyleInfo;

/**
 * 
 */
@Deprecated
public interface OutputColumnMapping {
    String getProperty();
    ExcelType getExcelType();
    String getHeader();    
    String getHyperlinkText();
    ColumnStyleInfo getColumnStyleInfo();
    String getDateFormat();
}
