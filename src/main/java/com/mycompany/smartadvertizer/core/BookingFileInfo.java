/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 
 */
public class BookingFileInfo {

    private Path filePath;
    private Path photoDir;
    private Path mapDir;
    private Path referenceFile;
    private String addressColumn;
    private String sideColumn;
    private String gidColumn;
    private Map<Year, ColumnRange> columnRangesByYear = new HashMap<>();

    private BookingFileInfo() {
    }
    
    public static BookingFileInfoBuilder getBuilder() {
        return new BookingFileInfoBuilder();
    }

    public static class BookingFileInfoBuilder {

        private final BookingFileInfo bookingFileInfo;

        private BookingFileInfoBuilder() {
            bookingFileInfo = new BookingFileInfo();
        }
        
        public BookingFileInfoBuilder filePath(String filePath) {
            bookingFileInfo.setFilePath(filePath);
            return this;
        }
        
        public BookingFileInfoBuilder addressColumn(String addressColumn) {
            bookingFileInfo.setAddressColumn(addressColumn);
            return this;
        }
        
        public BookingFileInfoBuilder sideColumn(String sideColumn) {
            bookingFileInfo.setSideColumn(sideColumn);
            return this;
        }
        
         public BookingFileInfoBuilder referenceFile(String filePath) {
            bookingFileInfo.setReferenceFile(filePath);
            return this;
        }
        
        public BookingFileInfoBuilder photoDir(String photoDir) {
            bookingFileInfo.setPhotoDir(photoDir);
            return this;
        }
        
        public BookingFileInfoBuilder mapDir(String mapDir) {
            bookingFileInfo.setMapDir(mapDir);
            return this;
        }
        
        public BookingFileInfoBuilder columnRangeByYear(Year year, ColumnRange columnRange) {
            bookingFileInfo.columnRangesByYear.put(year, columnRange);
            return this;
        }
        
        public BookingFileInfo build() {
            Objects.requireNonNull(bookingFileInfo.filePath);
            Objects.requireNonNull(bookingFileInfo.addressColumn);
            Objects.requireNonNull(bookingFileInfo.sideColumn);
            Objects.requireNonNull(bookingFileInfo.referenceFile);
            Objects.requireNonNull(bookingFileInfo.photoDir);
            Objects.requireNonNull(bookingFileInfo.mapDir);            
            return bookingFileInfo;
        }
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public Path getPhotoDir() {
        return photoDir;
    }

    public void setPhotoDir(String photoDir) {
        this.photoDir = Paths.get(photoDir);
    }

    public Path getMapDir() {
        return mapDir;
    }

    public void setMapDir(String mapDir) {
        this.mapDir = Paths.get(mapDir);
    }
    
    public Path getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(String referenceFile) {
        this.referenceFile = Paths.get(referenceFile);
    }

    public String getAddressColumn() {
        return addressColumn;
    }

    public void setAddressColumn(String addressColumn) {
        this.addressColumn = addressColumn;
    }

    public String getSideColumn() {
        return sideColumn;
    }

    public void setSideColumn(String sideColumn) {
        this.sideColumn = sideColumn;
    }

    public String getGidColumn() {
        return gidColumn;
    }
    
    public void addColumnRange(Year year, ColumnRange columnRange) {
        columnRangesByYear.put(year, columnRange);
    }
    
    public void removeColumnRange(Year year) {
        columnRangesByYear.remove(year);
    }
    
    public ColumnRange getColumnRange(Year year) {
        return columnRangesByYear.get(year);
    }    
}
