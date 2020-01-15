/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import com.mycompany.smartadvertizer.excelmapper.AppendableWriter;
import com.mycompany.smartadvertizer.excelmapper.AppendableXlsWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

/**
 *
 */
public class BillboardSideWriter {
    
    private AppendableWriter<BillboardSide> writer;
    private final Config config;
    private int skipRows = 1;
    
    @Autowired
    public BillboardSideWriter(Config config, ConversionService conversionService) {
        this.config = config;
        writer = new AppendableXlsWriter<>(BillboardSide.class);
        writer.setConversionService(conversionService);
        writer.setSkipFirstRows(skipRows);
        writer.setCreateHeadersRow(true);
        writer.setDefaultHeaderStyleInfo(config.getDefaultHeaderStyleInfo());
        writer.setDefaultColumnStyleInfo(config.getDefaultColumnStyleInfo());
    }
    
    public void write(String file, Sample sample) {        
        List<String> properties = sample.getOutputProperties();
        int propertyIndex = properties.indexOf("bookingStatuses");
        if (propertyIndex != -1) {
            List<String> firstPartProperties = properties.subList(0, propertyIndex);
            List<String> secondPartProperties = properties.subList(propertyIndex + 1,
                    properties.size());
            writer.write(file, sample.getBillboardSides(), firstPartProperties);
            writeBookingStatuses(file, sample.getBillboardSides(), propertyIndex,
                    sample.getMonths());
            int startColumn = firstPartProperties.size() + sample.getMonths().size();
            writer.append(file, sample.getBillboardSides(), secondPartProperties,
                    startColumn);
            createBookingStatusDescriptionRow(file, 2);
        } else {
            writer.write(file, sample.getBillboardSides(), sample.getOutputProperties());
        }
        
    }
    
    private void writeBookingStatuses(String file, List<BillboardSide> billboardSides,
            int startColumn, List<String> months) {
        try (InputStream ins = Files.newInputStream(Paths.get(file))) {
            Workbook workbook = new HSSFWorkbook(ins);
            Sheet sheet = workbook.getSheetAt(0);
            CellStyle bookedSideStyle = config.getBookedSideStyleInfo().toCellStyle(
                    workbook);
            
            CellStyle defaultHeaderStyle = null;
            if (config.getDefaultHeaderStyleInfo() != null) {
                defaultHeaderStyle = config.getDefaultHeaderStyleInfo().toCellStyle(
                        workbook);
            }
            int rowNumber = skipRows;
            Row headerRow = sheet.getRow(rowNumber++);
            int columnNumber = startColumn;
            for (String month : months) {
                Cell cell = headerRow.createCell(columnNumber++);
                cell.setCellValue(month);
                cell.setCellStyle(defaultHeaderStyle);
            }
            CellStyle freeSideStyle = config.getFreeSideStyleInfo().toCellStyle(workbook);
            for (BillboardSide bb : billboardSides) {
                columnNumber = startColumn;
                BookingStatus[] bookingStatuses = bb.getBookingStatuses();
                for (BookingStatus status : bookingStatuses) {
                    Cell cell = sheet.getRow(rowNumber).createCell(columnNumber++);
                    if (status.isBooked()) {
                        cell.setCellValue("0");
                        cell.setCellStyle(bookedSideStyle);
                    } else {
                        cell.setCellValue("1");
                        cell.setCellStyle(freeSideStyle);
                    }
                }
                rowNumber++;
            }
            ins.close();
            workbook.write(Files.newOutputStream(Paths.get(file)));
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(BillboardSideWriter.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }
    
    private void createBookingStatusDescriptionRow(String file, int skipColumns) {
        try (InputStream ins = Files.newInputStream(Paths.get(file))) {
            Workbook workbook = new HSSFWorkbook(ins);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(0);
            Cell freeStatusDescriptionCell = row.createCell(skipColumns);
            Cell bookedStatusDescriptionCell = row.createCell(++skipColumns);
            freeStatusDescriptionCell.setCellValue("1-свободно");
            freeStatusDescriptionCell.setCellStyle(config.getFreeSideStyleInfo().
                    toCellStyle(workbook));
            bookedStatusDescriptionCell.setCellValue("0-занято");
            bookedStatusDescriptionCell.setCellStyle(config.getBookedSideStyleInfo().
                    toCellStyle(workbook));
            workbook.write(Files.newOutputStream(Paths.get(file)));
            workbook.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

//    public void setDefaultColumnStyleInfo(ColumnStyleInfo defaultColumnStyleInfo) {
//        this.defaultColumnStyleInfo = defaultColumnStyleInfo;
//    }
//
//    public void setDefaultHeaderStyleInfo(CellStyleInfo defaultHeaderStyleInfo) {
//        this.defaultHeaderStyleInfo = defaultHeaderStyleInfo;
//    }
}
