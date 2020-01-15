/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper;

import com.mycompany.smartadvertizer.excelmapper.util.ReflectionUtils;
import com.mycompany.smartadvertizer.excelmapper.util.FileUtil;
import com.mycompany.smartadvertizer.excelmapper.util.Util;
import com.mycompany.smartadvertizer.excelmapper.mapping.InputColumnMapping;
import com.mycompany.smartadvertizer.excelmapper.mapping.InputColumnMappingInfo;
import com.mycompany.smartadvertizer.excelmapper.mapping.MappingAnotationParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.core.convert.ConversionService;

/**
 *
 * @param <T>
 */
public class XlsReader<T> implements Reader<T> {

    private Class<T> type;
    private Map<String, InputColumnMappingInfo> columnMappings;
    private ConversionService conversionService;
    private int skipFirstRows;
    private Map<String, Class<?>> propertyTypes = new HashMap<>();

    private static final String NULL_EXCEPTION_MESSAGE
            = "required property %s has null value in cell {%d, %d}";

    public XlsReader(Class<T> type) {
        this(type, MappingAnotationParser.findInputColumnMappingInfo(type));
    }
    
    public XlsReader(Class<T> type, int skipFirstRows) {
        this(type, MappingAnotationParser.findInputColumnMappingInfo(type), skipFirstRows);
    }

    public XlsReader(Class<T> type, List<InputColumnMappingInfo> columnMappings) {
        this.type = Objects.requireNonNull(type, "type can't be null");
        Objects.requireNonNull(columnMappings, "columnMappings can't be null");
        this.columnMappings = Util.toMap(columnMappings,
                InputColumnMappingInfo::getProperty);
    }

    public XlsReader(Class<T> type, List<InputColumnMappingInfo> columnMappings,
            int skipFirstRows) {
        this(type, columnMappings);
        setSkipFirstRows(skipFirstRows);
    }

    @Override
    public List<T> read(String file, List<String> properties) {
        FileUtil.checkFileExists(file);
        checkProperties(properties);
        List<T> entities = new ArrayList<>();
        try (HSSFWorkbook workBook = new HSSFWorkbook(Files.newInputStream(
                Paths.get(file)))) {
            HSSFSheet workSheet = workBook.getSheetAt(0);
            for (int i = skipFirstRows; i <= workSheet.getLastRowNum(); i++) {
                Row row = workSheet.getRow(i);                
                T entity = type.newInstance();
                for (String property : properties) {
                    InputColumnMapping columnMapping = columnMappings.get(property);
                    int column = columnMapping.getColumnIndex();
                    Cell cell = row.getCell(column);
                    Object value = null;
                    if ((cell == null || (value = getCellValue(cell)) == null)
                            && !columnMapping.isNullable()) {
                        throw new IllegalArgumentException(String.format(
                                NULL_EXCEPTION_MESSAGE, property, i, column));
                    }
                    setProperty(entity, property, value);
                }
                entities.add(entity);
            }
        } catch (IOException | ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
        return entities;
    }

    private void checkProperties(List<String> properties) {
        properties.forEach((String p) -> {
            if (!columnMappings.containsKey(p)) {
                throw new IllegalArgumentException("no mapping for propertie: " + p);
            }
        });
    }

    private void setProperty(Object obj, String property, Object value) {
        if (value == null) {
            return;
        }
        Class<?> targetType = propertyTypes.get(property);
        if (targetType == null) {
            targetType = ReflectionUtils.getPropertyType(type, property);
            propertyTypes.put(property, targetType);
        }

        Class<?> sourceType = value.getClass();
        if (sourceType != targetType) {
            if (conversionService != null && conversionService.canConvert(sourceType,
                    targetType)) {
                Object convertedValue = conversionService.convert(value, targetType);
                ReflectionUtils.setProperty(obj, property, convertedValue);
            } else {
                throw new IllegalArgumentException(
                        "source type differes from target type: "
                        + sourceType + " " + targetType);
            }
        } else {
            ReflectionUtils.setProperty(obj, property, value);
        }
    }

    protected Object getCellValue(Cell cell) {
        if (cell.getHyperlink() != null) {
            return cell.getHyperlink().getAddress();
        }

        switch (cell.getCellTypeEnum()) {
            case STRING: {
                return cell.getStringCellValue();
            }
            case NUMERIC: {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    return date.toInstant().atZone(ZoneId.systemDefault()).
                            toLocalDateTime();
                }
                return cell.getNumericCellValue();
            }
            case BOOLEAN: {
                return cell.getBooleanCellValue();
            }
            case BLANK: {
                return null;
            }
            case FORMULA: {
                switch (cell.getCachedFormulaResultTypeEnum()) {
                    case STRING: {
                        return cell.getStringCellValue();
                    }
                    case NUMERIC: {
                        return cell.getNumericCellValue();
                    }
                    case BOOLEAN: {
                        return cell.getBooleanCellValue();
                    }
                    default: {
                        throw new UnsupportedOperationException(String.format(
                                "does not support reading from cell(%d, %d) of type: %s",
                                cell.getRowIndex(), cell.getColumnIndex(), cell.
                                getCellTypeEnum().toString()));
                    }
                }
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
}
