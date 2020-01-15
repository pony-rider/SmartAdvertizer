/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.deprecated;

import com.mycompany.smartadvertizer.excelmapper.mapping.OutputColumnMapping;
import java.util.Date;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.convert.ConversionService;

/**
 *
 */
public abstract class AbstractPropertyWriter<T> implements PropertyWriter<T> {
    private final OutputColumnMapping mapping;
    private ConversionService conversionService;
    private Workbook workbook;

    public AbstractPropertyWriter(OutputColumnMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public void init(Workbook workbook, ConversionService conversionService) {
        
    }

    protected OutputColumnMapping getMapping() {
        return mapping;
    }

    protected ConversionService getConversionService() {
        return conversionService;
    }

    protected Workbook getWorkbook() {
        return workbook;
    }
    
    protected void setCellValue(Cell cell, OutputColumnMapping mapping, Object value) {
        if (value == null) {
            return;
        }
        switch (mapping.getExcelType()) {
            case STRING: {
                cell.setCellValue((String) value);
                break;
            }
            case NUMERIC: {
                cell.setCellValue((double) value);
                break;
            }

            case BOOLEAN: {
                cell.setCellValue((boolean) value);
                break;
            }
            case HYPERLINK: {
                CreationHelper createHelper = cell.getSheet().getWorkbook().
                        getCreationHelper();
                Hyperlink link = (Hyperlink) createHelper.createHyperlink(
                        HyperlinkType.URL);
                link.setAddress((String) value);
                cell.setHyperlink(link);
                cell.setCellValue(mapping.getHyperlinkText());
                break;
            }
            case DATE: {
//                CreationHelper createHelper = cell.getSheet().getWorkbook().
//                        getCreationHelper();
//                Optional.ofNullable(cell.getCellStyle())
//                        .orElse(cell.getSheet().getWorkbook().createCellStyle());
//                cell.getCellStyle().setDataFormat(createHelper.createDataFormat().
//                        getFormat("dd.mm.yyyy"));
                cell.setCellValue((Date) value);
            }

            default: {
                throw new UnsupportedOperationException(String.format(
                        "does not support reading from cell(%d, %d) of type: %s",
                        cell.getRowIndex(), cell.getColumnIndex(), cell.
                        getCellTypeEnum().toString()));
            }
        }
    }
}
