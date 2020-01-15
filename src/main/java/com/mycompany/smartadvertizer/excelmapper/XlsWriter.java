/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper;

import com.mycompany.smartadvertizer.excelmapper.util.ReflectionUtils;
import com.mycompany.smartadvertizer.excelmapper.util.Util;
import com.mycompany.smartadvertizer.excelmapper.mapping.ExcelType;
import com.mycompany.smartadvertizer.excelmapper.mapping.MappingAnotationParser;
import com.mycompany.smartadvertizer.excelmapper.mapping.OutputColumnMapping;
import com.mycompany.smartadvertizer.excelmapper.mapping.OutputColumnMappingInfo;
import com.mycompany.smartadvertizer.excelmapper.style.CellStyleInfo;
import com.mycompany.smartadvertizer.excelmapper.style.ColumnStyleInfo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.convert.ConversionService;

/**
 *
 *
 */
public class XlsWriter<T> implements Writer<T> {

    private ConversionService conversionService;
    private int skipFirstRows;
    private boolean createHeadersRow;
    protected Map<String, OutputColumnMappingInfo> columnMappings;
    protected ColumnStyleInfo defaultColumnStyleInfo;
    protected CellStyleInfo defaultHeaderStyleInfo;

    public XlsWriter(Class<T> type) {
        this(MappingAnotationParser.findOutputColumnMappingInfo(type));
    }

    public XlsWriter(Class<T> type, int skipFirstRows) {
        this(MappingAnotationParser.findOutputColumnMappingInfo(type), skipFirstRows);
    }

    public XlsWriter(List<OutputColumnMappingInfo> columnMappings) {
        Objects.requireNonNull(columnMappings, "columnMappings can't be null");
        this.columnMappings = Util.toMap(columnMappings,
                OutputColumnMappingInfo::getProperty);
    }

    public XlsWriter(List<OutputColumnMappingInfo> columnMappings, int skipFirstRows) {
        this(columnMappings);
        setSkipFirstRows(skipFirstRows);
    }

    @Override
    public void write(String file, List<T> entities, String... properties) {
        write(file, entities, Arrays.asList(properties));
    }

    protected void checkProperties(List<String> properties) {
        properties.forEach((String p) -> {
            if (!columnMappings.containsKey(p)) {
                throw new IllegalArgumentException("no mapping for propertie: " + p);
            }
        });
    }

    @Override
    public void write(String file, List<T> entities, List<String> properties) {
        checkProperties(properties);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        int rowNumber = skipFirstRows;
        if (createHeadersRow) {
            Row row = sheet.createRow(rowNumber++);
            createHeadersRow(row, properties);            
        }

        CellStyle defaultColumnStyle = null;
        if (defaultColumnStyleInfo != null) {
            defaultColumnStyle = defaultColumnStyleInfo.toCellStyle(workbook);
        }

        Map<String, CellStyle> styles = initColumnStyles(workbook, properties,
                defaultColumnStyle);

        for (T entity : entities) {
            Row row = sheet.createRow(rowNumber++);
            int columnNumber = 0;
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
            ColumnStyleInfo columnStyle = columnMappings.get(properties.get(i)).getColumnStyleInfo();
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

        try {
            workbook.write(Files.newOutputStream(Paths.get(file)));
            workbook.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected Map<String, CellStyle> initColumnStyles(Workbook workbook,
            List<String> properties, CellStyle defaultColumnStyle) {
        Map<String, CellStyle> styles = new HashMap<>();
        for (String property : properties) {
            OutputColumnMappingInfo mapping = columnMappings.get(property);
            if (mapping == null) {
                continue;
            }
            ColumnStyleInfo styleInfo = mapping.getColumnStyleInfo();
            if (styleInfo != null) {
                if (mapping.getExcelType() == ExcelType.DATE) {
                    styleInfo.setDateFormat(mapping.getDateFormat());
                }
                styles.put(mapping.getProperty(), styleInfo.toCellStyle(workbook));
            } else if (mapping.getExcelType() == ExcelType.DATE) {
                CellStyle style = workbook.createCellStyle();
                if (defaultColumnStyle != null) {
                    style.cloneStyleFrom(defaultColumnStyle);
                }
                addDateFormat(workbook, style, mapping.getDateFormat());
                styles.put(mapping.getProperty(), style);
            }
        }
        return styles;
    }

    protected void addDateFormat(Workbook workbook, CellStyle style, String dateFormat) {
        CreationHelper createHelper = workbook.getCreationHelper();
        short formatIndex = createHelper.createDataFormat().getFormat(dateFormat);
        style.setDataFormat(formatIndex);
    }

    private void createHeadersRow(Row row, List<String> properties) {
        CellStyle defaultStyle = null;
        if (defaultColumnStyleInfo != null) {
            defaultStyle = defaultHeaderStyleInfo.
                    toCellStyle(row.getSheet().getWorkbook());
        }
        int columnNumber = 0;
        for (String property : properties) {
            OutputColumnMapping columnMapping = columnMappings.get(property);
            Cell cell = row.createCell(columnNumber++);
            cell.setCellValue(columnMapping.getHeader());
            if (defaultStyle != null) {
                cell.setCellStyle(defaultStyle);
            }
        }
    }

    protected void setCellValue(Cell cell, OutputColumnMappingInfo mapping, Object value) {
        if (value == null) {
            return;
        }
        if (value.getClass() != String.class && conversionService == null) {
            throw new IllegalArgumentException(String.format(
                    "property type %s doesn't match to excel type %s", value.getClass().
                    toString(), mapping.getExcelType().toString()));
        }
        switch (mapping.getExcelType()) {
            case STRING: {
                if (value.getClass() != String.class) {
                    String convertedValue = conversionService.convert(value, String.class);
                    cell.setCellValue(convertedValue);
                } else {
                    cell.setCellValue((String) value);
                }
                break;

            }
            case NUMERIC: {
                if (value.getClass() != double.class) {
                    double convertedValue = conversionService.convert(value, double.class);
                    cell.setCellValue(convertedValue);
                } else {
                    cell.setCellValue((double) value);
                }
                break;

            }

            case BOOLEAN: {
                if (value.getClass() != boolean.class) {
                    boolean convertedValue = conversionService.convert(value,
                            boolean.class);
                    cell.setCellValue(convertedValue);
                } else {
                    cell.setCellValue((boolean) value);
                }
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
                if (value.getClass() != Date.class) {
                    Date convertedValue = conversionService.convert(value, Date.class);
                    cell.setCellValue(convertedValue);
                } else {
                    cell.setCellValue((Date) value);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException(String.format(
                        "does not support reading from cell(%d, %d) of type: %s",
                        cell.getRowIndex(), cell.getColumnIndex(), cell.
                        getCellTypeEnum().toString()));
            }
        }
    }

    @Override
    public void setCreateHeadersRow(boolean createHeadersRow) {
        this.createHeadersRow = createHeadersRow;
    }

    @Override
    public void setSkipFirstRows(int skipFirstRows) {
        if (skipFirstRows < 0) {
            throw new IllegalArgumentException(
                    "rows to skip number must be >= 0: " + skipFirstRows);
        }
        this.skipFirstRows = skipFirstRows;
    }

    @Override
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public void setDefaultColumnStyleInfo(ColumnStyleInfo defaultColumnStyleInfo) {
        this.defaultColumnStyleInfo = defaultColumnStyleInfo;
    }

    @Override
    public void setDefaultHeaderStyleInfo(CellStyleInfo defaultHeaderStyleInfo) {
        this.defaultHeaderStyleInfo = defaultHeaderStyleInfo;
    }

    public int getSkipFirstRows() {
        return skipFirstRows;
    }

    public boolean getCreateHeadersRow() {
        return createHeadersRow;
    }
}
