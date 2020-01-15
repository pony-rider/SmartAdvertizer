/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

/**
 *
 * @author chelenver
 */
@Component
public class XlsReaderFactory implements ReaderFactory {
    private ConversionService conversionService;

    @Autowired
    public XlsReaderFactory(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public <T> Reader<T> createReader(Class<T> type, int skipRows) {
        return new XlsReader<>(type, skipRows);
    }

    @Override
    public <T> Reader<T> createReader(Class<T> type) {
        return new XlsReader<>(type);
    }
}
