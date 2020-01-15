/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper;

import com.mycompany.smartadvertizer.excelmapper.style.CellStyleInfo;
import com.mycompany.smartadvertizer.excelmapper.style.ColumnStyleInfo;
import java.util.List;
import org.springframework.core.convert.ConversionService;

/**
 *
 * 
 */
public interface Writer<T> {

    void write(String file, List<T> entities, String... properties);
    
    void write(String file, List<T> entities, List<String> properties);
    
    void setSkipFirstRows(int skipFirstRows);
    
    void setConversionService(ConversionService conversionService);
    
    void setCreateHeadersRow(boolean create);
    
    void setDefaultHeaderStyleInfo(CellStyleInfo cellStyleInfo);
    
    void setDefaultColumnStyleInfo(ColumnStyleInfo columnStyleInfo);
}
