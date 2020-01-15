/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper;

import java.util.List;
import org.springframework.core.convert.ConversionService;

/**
 *
 *
 * @param <T>
 */
public interface Reader<T> {

    List<T> read(String file, List<String> properties);

    void setSkipFirstRows(int skipFirstRows);
    
    void setConversionService(ConversionService conversionService);
}
