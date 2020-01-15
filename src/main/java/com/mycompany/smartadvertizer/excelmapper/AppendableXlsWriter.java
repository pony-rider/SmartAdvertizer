/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper;

import com.mycompany.smartadvertizer.excelmapper.util.ReflectionUtils;
import com.mycompany.smartadvertizer.excelmapper.mapping.OutputColumnMapping;
import com.mycompany.smartadvertizer.excelmapper.mapping.OutputColumnMappingInfo;
import com.mycompany.smartadvertizer.excelmapper.style.ColumnStyleInfo;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 */
public class AppendableXlsWriter<T> extends XlsWriter<T> implements AppendableWriter<T> {

    public AppendableXlsWriter(Class<T> type) {
        super(type);
    }

    public AppendableXlsWriter(Class<T> type, int skipFirstRows) {
        super(type, skipFirstRows);
    }

    public AppendableXlsWriter(List<OutputColumnMappingInfo> columnMappings) {
        super(columnMappings);
    }

    public AppendableXlsWriter(List<OutputColumnMappingInfo> columnMappings,
            int skipFirstRows) {
        super(columnMappings, skipFirstRows);
    }

    private void appendHeaders(Row row, List<String> properties, int startColumn) {
        CellStyle defaultStyle = null;
        if (defaultHeaderStyleInfo != null) {
            defaultStyle = defaultHeaderStyleInfo.
                    toCellStyle(row.getSheet().getWorkbook());
        }
        int columnNumber = startColumn;
        for (String property : properties) {
            OutputColumnMappingInfo columnMapping = columnMappings.get(property);
            Cell cell = row.createCell(columnNumber++);
            cell.setCellValue(columnMapping.getHeader());
            if (defaultStyle != null) {
                cell.setCellStyle(defaultStyle);
            }
        }
    }

    @Override
    public void append(String file, List<T> entities, List<String> properties,
            int startColumn) {
        checkProperties(properties);
        try (InputStream ins = Files.newInputStream(Paths.get(file))) {
            Workbook workbook = new HSSFWorkbook(ins);
            Sheet sheet = workbook.getSheetAt(0);
            int rowNumber = getSkipFirstRows();
            if (this.getCreateHeadersRow()) {
                Row row = sheet.getRow(rowNumber++);                
                appendHeaders(row, properties, startColumn);
            }

            CellStyle defaultColumnStyle = null;
            if (defaultColumnStyleInfo != null) {
                defaultColumnStyle = defaultColumnStyleInfo.toCellStyle(workbook);
            }

            Map<String, CellStyle> styles = initColumnStyles(workbook, properties,
                    defaultColumnStyle);

            for (T entity : entities) {
                Row row = sheet.getRow(rowNumber++);
                int columnNumber = startColumn;
                for (String property : properties) {
                    OutputColumnMappingInfo columnMapping = columnMappings.get(property);
                    Cell cell = row.createCell(columnNumber++);
                    Object value = ReflectionUtils.getProperty(entity, columnMapping.
                            getProperty());
                    setCellValue(cell, columnMapping, value);
                    CellStyle style = styles.get(property);
                    if (style != null) {
                        cell.setCellStyle(style);
                    } else {
                        cell.setCellStyle(defaultColumnStyle);
                    }
                }
            }

            for (int i = 0; i < properties.size(); i++) {
                ColumnStyleInfo columnStyle = columnMappings.get(properties.get(i)).
                        getColumnStyleInfo();
                if (columnStyle == null) {
                    columnStyle = defaultColumnStyleInfo;
                }
                if (columnStyle != null) {
                    if (columnStyle.getAutoWidth()) {
                        sheet.autoSizeColumn(i);
                    } else if (columnStyle.getWidth() > 0) {
                        sheet.setColumnWidth(i, columnStyle.getWidth());
                    }
                }
            }
            ins.close();
            workbook.write(Files.newOutputStream(Paths.get(file)));
            workbook.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
