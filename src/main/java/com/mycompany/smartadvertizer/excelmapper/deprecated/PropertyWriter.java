/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.smartadvertizer.excelmapper.deprecated;

import com.mycompany.smartadvertizer.excelmapper.style.ColumnStyleInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.convert.ConversionService;

/**
 *
 */
public interface PropertyWriter<T> {
    void init(Workbook workbook, ConversionService conversionService);
    int writeHeaders(Row row, int column);   
    int write(T entity, Row row, int column);   
    
}
