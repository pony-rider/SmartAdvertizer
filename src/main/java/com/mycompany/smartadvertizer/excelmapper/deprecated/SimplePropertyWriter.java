/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.deprecated;

import com.mycompany.smartadvertizer.excelmapper.util.ReflectionUtils;
import com.mycompany.smartadvertizer.excelmapper.mapping.ExcelType;
import com.mycompany.smartadvertizer.excelmapper.mapping.OutputColumnMapping;
import com.mycompany.smartadvertizer.excelmapper.style.CellStyleInfo;
import java.util.Date;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.convert.ConversionService;

/**
 *
 */
public class SimplePropertyWriter<T> implements PropertyWriter<T> {
    private final OutputColumnMapping mapping;
    private ConversionService conversionService;
    private CellStyle style;

    public SimplePropertyWriter(OutputColumnMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public void init(Workbook workbook, ConversionService conversionService) {
        this.conversionService = conversionService;
        CellStyleInfo styleInfo = mapping.getColumnStyleInfo();        
        if (styleInfo != null) {
            if (mapping.getExcelType() == ExcelType.DATE) {
                styleInfo.setDateFormat(mapping.getDateFormat());
            }
            style = styleInfo.toCellStyle(workbook);
        }
    }

    @Override
    public int write(T entity, Row row, int column) {
        Cell cell = row.createCell(column);
        Object value = ReflectionUtils.getProperty(entity, mapping.getProperty());
        setCellValue(cell, value);
        if (style != null) {
            cell.setCellStyle(style);
        }
        return column + 1;
    }
    
     @Override
    public int writeHeaders(Row row, int column) {
        Cell cell = row.createCell(column);
        cell.setCellValue(mapping.getHeader());
        return column + 1;
    }
    
    protected void setCellValue(Cell cell, Object value) {
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

    public void setColumnWidth(int column) {

    }

   

}
