/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import com.mycompany.smartadvertizer.excelmapper.util.FileUtil;
import com.mycompany.smartadvertizer.core.filter.FilterChain;
import com.mycompany.smartadvertizer.core.filter.FilterChainDescription;
import com.mycompany.smartadvertizer.core.filter.FilterChainFactory;
import com.mycompany.smartadvertizer.excelmapper.Reader;
import com.mycompany.smartadvertizer.excelmapper.util.ReflectionUtils;
import com.mycompany.smartadvertizer.excelmapper.util.Util;
import com.mycompany.smartadvertizer.excelmapper.XlsReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.convert.ConversionService;

/**
 *
 */
public class BillboardSideReader {

    private final Reader<BillboardSide> reader;
    private final Config config;    
    private FilterChainFactory filterChainFactory;

    public BillboardSideReader(Config config, ConversionService conversionService,
            FilterChainFactory filterChainFactory) {
        this.reader = new XlsReader<>(BillboardSide.class, 1);
        reader.setConversionService(conversionService);
        this.config = config;
        this.filterChainFactory = filterChainFactory;
    }

    public List<BillboardSide> getFreeBillboardSides(YearMonth start, YearMonth end,
            List<String> properties, FilterChainDescription filterChainDescription) {
        return getFreeBillboardSides(TimeUtil.getMonths(start, end), properties,
                filterChainDescription);
    }

    public List<BillboardSide> getFreeBillboardSides(List<YearMonth> months,
            List<String> properties, FilterChainDescription filterChainDescription) {
        TimeUtil.checkMonthRange(months);
        FilterChain filterChain = filterChainFactory.createFilterChain(
                BillboardSide.class, filterChainDescription);
        List<BillboardSide> billboardSides = getFreeBillboardSides(months, properties);
        filter(billboardSides, filterChain);
        return billboardSides;
    }

    public List<BillboardSide> getFreeBillboardSides(YearMonth start, YearMonth end,
            List<String> properties) {
        return getFreeBillboardSides(TimeUtil.getMonths(start, end), properties);
    }

    public List<BillboardSide> getFreeBillboardSides(List<YearMonth> months,
            List<String> properties) {
        TimeUtil.checkMonthRange(months);
        List<BillboardSide> billboardSides = readBillboardSidesFromReferenceFile(
                properties);
        fillBookingStatuses(months, billboardSides);
        filterBookedBillboardSides(billboardSides);
        return billboardSides;
    }

    private List<BillboardSide> readBillboardSidesFromReferenceFile(
            List<String> properties) {
        Set<String> propertySet = new LinkedHashSet<>(properties);
        propertySet.add("address");
        propertySet.add("side");
        List<String> requiredProperties = propertySet.stream().
                collect(Collectors.toList());
        String refFile = config.getBookingFileInfo().getReferenceFile().toString();
        return reader.read(refFile, requiredProperties);
    }

    public void filter(List<BillboardSide> billboardSides,
            FilterChainDescription filterChainDescription) {
        FilterChain filterChain = filterChainFactory.createFilterChain(
                BillboardSide.class, filterChainDescription);
        filter(billboardSides, filterChain);
    }

    private void filter(List<BillboardSide> billboardSides, FilterChain filterChain) {
        for (Iterator<BillboardSide> bbIterator = billboardSides.iterator();
                bbIterator.hasNext();) {
            BillboardSide bb = bbIterator.next();
            if (!filterChain.accept(bb)) {
                bbIterator.remove();
            }
        }
    }

    private void filterBookedBillboardSides(List<BillboardSide> billboardSides) {
        for (Iterator<BillboardSide> iterator = billboardSides.iterator(); iterator.
                hasNext();) {
            BillboardSide billboardSide = iterator.next();
            boolean isBookedForAllPeriod = true;
            for (BookingStatus bookingStatus : billboardSide.getBookingStatuses()) {
                if (!bookingStatus.isBooked()) {
                    isBookedForAllPeriod = false;
                    break;
                }
            }
            if (isBookedForAllPeriod) {
                iterator.remove();
            }
        }
    }

    private void fillBookingStatuses(List<YearMonth> motnhs,
            List<BillboardSide> billboardSides) {
        BookingFileInfo bookingFileInfo = config.getBookingFileInfo();

        Map<BillboardSide, BillboardSide> billboardSidesMap = Util.toMap(billboardSides,
                Function.identity());
        try (HSSFWorkbook book = new HSSFWorkbook(Files.newInputStream(
                bookingFileInfo.getFilePath()))) {
            HSSFSheet sheet = book.getSheetAt(0);
            int addressColumn = CellReference.convertColStringToIndex(
                    bookingFileInfo.getAddressColumn());
            int sideColumnIndex = CellReference.convertColStringToIndex(
                    bookingFileInfo.getSideColumn());

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell addressCell = row.getCell(addressColumn);
                Cell sideCell = row.getCell(sideColumnIndex);
                Objects.requireNonNull(addressCell, String.format(
                        "address cell is null on row: %d, column %d", i, addressColumn));
                Objects.requireNonNull(sideCell, String.format("side cell is null on "
                        + "row: %d, column %d", i, sideColumnIndex));
                String address = addressCell.getStringCellValue();
                String sideIndex = sideCell.getStringCellValue();
                BillboardSide billboardSide = billboardSidesMap.get(new BillboardSide(
                        address,
                        sideIndex));
                if (billboardSide != null) {
                    BookingStatus[] bookingStatuses = new BookingStatus[motnhs.size()];
                    int j = 0;
                    for (YearMonth month : motnhs) {
                        int index = getColumnIndex(month);
                        Cell cell = row.getCell(index);
                        BookingStatus bookingStatus = getBookingStatus(cell);
                        bookingStatuses[j++] = bookingStatus;
                    }
                    billboardSide.setBookingStatuses(bookingStatuses);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<BillboardSide> readBillboardSides(String file, String addressColumn,
            String sideColumn) {
        FileUtil.checkFileExists(file);
        Path filePath = Paths.get(file);
        List<BillboardSide> billboardSides = new ArrayList<>();
        try (Workbook book = new HSSFWorkbook(Files.newInputStream(filePath))) {
            Sheet sheet = book.getSheetAt(0);
            int firstRow = findRowsToSkip(sheet);
            int addressColumnIndex = CellReference.convertColStringToIndex(
                    addressColumn);
            int sideColumnIndex = CellReference.convertColStringToIndex(
                    sideColumn);
            for (int i = firstRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell addressCell = row.getCell(addressColumnIndex);
                Cell sideCell = row.getCell(sideColumnIndex);
                Objects.requireNonNull(addressCell, String.format(
                        "address cell is null on row: %d, column %d",
                        i, addressColumnIndex));
                Objects.requireNonNull(sideCell, String.format(
                        "side cell is null on row: %d, column %d", i,
                        sideColumnIndex));
                String address = addressCell.getStringCellValue();
                String sideIndex = sideCell.getStringCellValue();
                BillboardSide billboardSide = new BillboardSide(address,
                        sideIndex);
                billboardSides.add(billboardSide);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return billboardSides;
    }

    public int findRowsToSkip(Sheet sheet) {
        int rowsToSkip = sheet.getFirstRowNum();
        while (rowsToSkip <= sheet.getLastRowNum()) {
            Row row = sheet.getRow(rowsToSkip);
            Cell cell = row.getCell(0);
            if (cell == null || cell.getCellTypeEnum() == CellType.BLANK
                    || isCellColored(cell.getCellStyle())) {
                rowsToSkip++;
            } else {
                break;
            }
        }
        return rowsToSkip;
    }

    public void fillWithPropertiesFromReference(List<BillboardSide> billboardSides,
            List<String> properties) {
        Map< BillboardSide, BillboardSide> billboardSidesWithPropertiesMap = Util.toMap(
                readBillboardSidesFromReferenceFile(properties), Function.identity());
        for (BillboardSide bb : billboardSides) {
            BillboardSide bbFromRef = billboardSidesWithPropertiesMap.get(bb);
            if (bbFromRef == null) {
                throw new IllegalArgumentException(
                        "BillboardSide isn't found in reference file: " + bb);
            }
            ReflectionUtils.copyProperties(bbFromRef, bb, properties);
        }
    }

    private int getColumnIndex(YearMonth yearMonth) {
        ColumnRange columnRange = config.getBookingFileInfo().getColumnRange(Year.of(
                yearMonth.getYear()));
        int monthNumber = yearMonth.getMonthValue();
        return CellReference.convertColStringToIndex(columnRange.getStartColumn())
                + monthNumber - 1;
    }

    private BookingStatus getBookingStatus(Cell cell) {
        if (cell == null) {
            return BookingStatus.free();
        }
        String company = cell.getStringCellValue();
        if (company == null || company.isEmpty()) {
            return BookingStatus.free();
        }
        if (isCellColored(cell.getCellStyle())) {
            return canBeRelocated(company) ? BookingStatus.free()
                    : BookingStatus.booked(company);

        } else {
            return isHardReserved(company) ? BookingStatus.booked(company)
                    : BookingStatus.reserved(company);
        }
    }

    private boolean isCellColored(CellStyle style) {
        if (style.getFillPatternEnum() == FillPatternType.NO_FILL) {
            return false;
        }
        HSSFColor background = HSSFColor.toHSSFColor(style.
                getFillBackgroundColorColor());
        HSSFColor foreground = HSSFColor.toHSSFColor(style.
                getFillForegroundColorColor());
        //TODO: rewrite using predifined colors instead of manually defined
        //HSSFColor.HSSFColorPredefined.WHITE
        short[] white = {255, 255, 255};
        short[] backRgb = background.getTriplet();
        short[] foreRgb = foreground.getTriplet();
        return !(Arrays.equals(white, backRgb) || Arrays.equals(white, foreRgb));
    }

    private boolean canBeRelocated(String company) {
        return config.getIgnoredClients().contains(company.toLowerCase());
    }

    private boolean isHardReserved(String company) {
        return config.getVipClients().contains(company);
    }

}
