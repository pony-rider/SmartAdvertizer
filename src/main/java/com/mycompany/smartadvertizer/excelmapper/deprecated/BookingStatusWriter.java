/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.deprecated;

import com.mycompany.smartadvertizer.core.BillboardSide;
import com.mycompany.smartadvertizer.core.BookingStatus;
import com.mycompany.smartadvertizer.excelmapper.style.CellStyleInfo;
import com.mycompany.smartadvertizer.excelmapper.style.ColumnStyleInfo;
import java.time.YearMonth;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.convert.ConversionService;

/**
 *
 */
public class BookingStatusWriter implements PropertyWriter<BillboardSide> {

    private CellStyleInfo bookedSideStyleInfo = new ColumnStyleInfo(true).setColor(
            IndexedColors.RED.getIndex());
    private CellStyleInfo freeSideStyleInfo = new ColumnStyleInfo(true).setColor(
            IndexedColors.GREEN.getIndex());
    private CellStyle bookedSideStyle;
    private CellStyle freeSideStyle;
    private CellStyle defaultColumnStyle;
    private CellStyle defaultHeaderStyle;
    private List<String> headers;

    public void setDefaultColumnStyle(CellStyle defaultColumnStyle) {
        this.defaultColumnStyle = defaultColumnStyle;
    }

    public void setDefaultHeaderStyle(CellStyle defaultHeaderStyle) {
        this.defaultHeaderStyle = defaultHeaderStyle;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }
    
    @Override
    public void init(Workbook workbook, ConversionService conversionService) {
        bookedSideStyle = bookedSideStyleInfo.toCellStyle(workbook);
        freeSideStyle = freeSideStyleInfo.toCellStyle(workbook);        
    }

    @Override
    public int write(BillboardSide entity, Row row, int column) {
        int columnNumber = column;
        BookingStatus[] bookingStatuses = entity.getBookingStatuses();
        for (BookingStatus status : bookingStatuses) {
            Cell cell = row.createCell(columnNumber++);
            if (status.isFree()) {
                cell.setCellValue("1");
                cell.setCellStyle(freeSideStyle);
            } else {
                cell.setCellValue("0");
                cell.setCellStyle(bookedSideStyle);
            }
        }
        return columnNumber;
    }

    @Override
    public int writeHeaders(Row row, int column) {
        if (headers == null) {
            return column;
        }
        int columnNumber = column;
        for (String header : headers) {
            Cell cell = row.createCell(columnNumber++);
            cell.setCellValue(header);
            cell.setCellStyle(defaultHeaderStyle);
        }
        return columnNumber;
    }
}
