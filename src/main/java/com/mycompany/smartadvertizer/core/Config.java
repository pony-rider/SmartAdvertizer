/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.mycompany.smartadvertizer.core.filter.FilterChainDescription;
import com.mycompany.smartadvertizer.core.filter.FilterDescriptor;
import com.mycompany.smartadvertizer.core.filter.FilterOperator;
import com.mycompany.smartadvertizer.excelmapper.mapping.ColumnMappingInfo;
import com.mycompany.smartadvertizer.excelmapper.mapping.ExcelType;
import com.mycompany.smartadvertizer.excelmapper.mapping.InputColumnMapping;
import com.mycompany.smartadvertizer.excelmapper.mapping.OutputColumnMapping;
import com.mycompany.smartadvertizer.excelmapper.style.CellStyleInfo;
import com.mycompany.smartadvertizer.excelmapper.style.ColumnStyleInfo;
import java.nio.file.Path;
import java.time.Year;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Config {

    private BookingFileInfo bookingFile;
    private Map<String, ColumnMappingInfo> columnMappings;
    private Map<String, InputColumnMapping> inputColumnMappings;
    private Map<String, OutputColumnMapping> outputColumnMappings;

    private Map<String, List<String>> propertySets = new HashMap<>();
    private Set<String> vipClients = new HashSet<>();
    private Set<String> badBillboardSideRaitings = new HashSet<>();
    private Set<String> ignoredClients = new HashSet<>();
    private CellStyleInfo bookedSideStyleInfo = new ColumnStyleInfo(true).setColor(
            IndexedColors.RED.getIndex());
    private CellStyleInfo freeSideStyleInfo = new ColumnStyleInfo(true).setColor(
            IndexedColors.GREEN.getIndex());
    private ColumnStyleInfo defaultColumnStyleInfo = new ColumnStyleInfo(true)
            .setAutoWidth(true);
    private CellStyleInfo defaultHeaderStyleInfo = new CellStyleInfo(true, true)
            .setAlignment(HorizontalAlignment.CENTER)
            .setColor(IndexedColors.PALE_BLUE.getIndex());
    private Map<String, SamplePreferences> samplePreferencesMap = new HashMap<>();
    private Map<String, FilterChainDescription> filterChainDescriptionMap = new HashMap<>();

    {
        defaultColumnStyleInfo.setAlignment(HorizontalAlignment.CENTER);
    }

    {
        String path = "src/main/resources/";

        bookingFile = BookingFileInfo.getBuilder()
                .filePath(path + "xls/Адреска для тестов.xls")
                .addressColumn("E").sideColumn("D")
                .photoDir(path + "фото сторон")
                .referenceFile(path + "xls/общий справочник.xls")
                .mapDir(path + "КАРТЫ 2018")
                .columnRangeByYear(Year.of(2019), new ColumnRange("G", "R"))
                .columnRangeByYear(Year.of(2020), new ColumnRange("U", "AF"))
                .build();
    }

    {
        columnMappings = new LinkedHashMap<>();
        ColumnMappingInfo side = new ColumnMappingInfo("side", "D", "Сторона").setNullable(false);
        ColumnMappingInfo address = new ColumnMappingInfo("address", "E", "Адрес")
                .setNullable(false);
        ColumnMappingInfo region = new ColumnMappingInfo("region", "F", "Район");
        ColumnMappingInfo rating = new ColumnMappingInfo("rating", "G", "Рейтинг");
        ColumnMappingInfo longitude = new ColumnMappingInfo("longitude", "H", "Широта",
                ExcelType.NUMERIC);
        ColumnMappingInfo latitude = new ColumnMappingInfo("latitude", "I", "Долгота",
                ExcelType.NUMERIC);
        //ColumnMapping geoPoint = new ColumnMapping("geoPoint", "N");
        ColumnMappingInfo lighting = new ColumnMappingInfo("lighting", "K", "Освещение");

        ColumnMappingInfo photo = new ColumnMappingInfo("photo", "L", "Фото");
        ColumnMappingInfo map = new ColumnMappingInfo("map", "M", "Карта");
        ColumnMappingInfo photoLink = ColumnMappingInfo.hyperlinkMapping("photoLink", "N", "фото",
                "фото");
        ColumnMappingInfo mapLink = ColumnMappingInfo.hyperlinkMapping("mapLink", "O", "Карта",
                "карта");
        ColumnMappingInfo construction_type = new ColumnMappingInfo("constructionType", "R",
                "Тип конструкции");
        ColumnMappingInfo size = new ColumnMappingInfo("size", "S", "Размер");
        ColumnMappingInfo price = new ColumnMappingInfo("price", "V", "Стоимость",
                ExcelType.NUMERIC);
        ColumnMappingInfo priceForAdAgency = new ColumnMappingInfo("priceForAdAgency", "T",
                "Прайс", ExcelType.NUMERIC);
        ColumnMappingInfo discountedPriceForAdAgency = new ColumnMappingInfo(
                "discountedPriceForAdAgency", "U", "Стоимость для РА",
                ExcelType.NUMERIC);
        ColumnMappingInfo firstInstallationPrice = new ColumnMappingInfo(
                "firstInstallationPrice", "W", "Монтаж", ExcelType.NUMERIC);
        ColumnMappingInfo additonalInstallationPrice = new ColumnMappingInfo(
                "additionalInstallationPrice", "X", "Доп. монтаж", ExcelType.NUMERIC);
        ColumnMappingInfo material = new ColumnMappingInfo("material", "Y", "Материал");
        ColumnMappingInfo printingPrice = new ColumnMappingInfo("printingPrice", "Z", "Печать",
                ExcelType.NUMERIC);
        ColumnMappingInfo tax = new ColumnMappingInfo("tax", "AA", "Налог");
        ColumnMappingInfo endOfLicence = new ColumnMappingInfo("endOfLicence", "AB", "Разрешение",
                ExcelType.DATE);

        columnMappings.put(side.getProperty(), side);
        columnMappings.put(address.getProperty(), address);
        columnMappings.put(region.getProperty(), region);
        columnMappings.put(rating.getProperty(), rating);
        columnMappings.put(longitude.getProperty(), longitude);
        columnMappings.put(latitude.getProperty(), latitude);
        columnMappings.put(photo.getProperty(), photo);
        columnMappings.put(map.getProperty(), map);
        columnMappings.put(photoLink.getProperty(), photoLink);
        columnMappings.put(mapLink.getProperty(), mapLink);
        columnMappings.put(lighting.getProperty(), lighting);
        columnMappings.put(construction_type.getProperty(), construction_type);
        columnMappings.put(size.getProperty(), size);
        columnMappings.put(price.getProperty(), price);
        columnMappings.put(priceForAdAgency.getProperty(), priceForAdAgency);
        columnMappings.put(discountedPriceForAdAgency.getProperty(),
                discountedPriceForAdAgency);
        columnMappings.put(firstInstallationPrice.getProperty(), firstInstallationPrice);
        columnMappings.put(additonalInstallationPrice.getProperty(),
                additonalInstallationPrice);
        columnMappings.put(material.getProperty(), material);
        columnMappings.put(printingPrice.getProperty(), printingPrice);
        columnMappings.put(tax.getProperty(), tax);
        columnMappings.put(endOfLicence.getProperty(), endOfLicence);

        ColumnStyleInfo sideColumnStyleInfo = new ColumnStyleInfo(true, false, true);
        sideColumnStyleInfo.setAlignment(HorizontalAlignment.CENTER);
        side.setColumnStyleInfo(sideColumnStyleInfo);

        ColumnStyleInfo addressColumnStyleInfo = new ColumnStyleInfo(true).setWidth(40);
        addressColumnStyleInfo.setAlignment(HorizontalAlignment.CENTER);
        address.setColumnStyleInfo(addressColumnStyleInfo);

        inputColumnMappings = new LinkedHashMap<>();
        outputColumnMappings = new LinkedHashMap<>();

        for (Map.Entry<String, ColumnMappingInfo> entry : columnMappings.entrySet()) {
            String property = entry.getKey();
            ColumnMappingInfo columnMapping = entry.getValue();
            inputColumnMappings.put(property, columnMapping);
            outputColumnMappings.put(property, columnMapping);
        }
    }

    {
        propertySets.put("base_set", Arrays.asList("address", "side", "bookingStatuses",
                "lighting", "constructionType", "size", "price", "firstInstallationPrice",
                "additionalInstallationPrice", "material", "printingPrice"));

        propertySets.put("buing_agency_set", Arrays.asList("address", "side",
                "bookingStatuses", "lighting", "constructionType", "size",
                "priceForAdAgency", "discountedPriceForAdAgency", "firstInstallationPrice",
                "additionalInstallationPrice", "material", "printingPrice"));

        vipClients.addAll(Arrays.asList("Рехт", "рэхт", "METRO"));

        badBillboardSideRaitings.addAll(Arrays.asList("x", "xx", "х.х", "х", "хх",
                "щит неисправен", "снесли", "ушла"
        ));

        ignoredClients.addAll(Arrays.asList(
                "избирком", "Аренда", "шансон новый", "социалка", "Социалка", "шансон",
                "Энерджи", "АТ Меди"
        ));

        FilterChainDescription badRatingsFilterChain = FilterChainDescription.
                createSingleTypeFilterChainDescription("raiting", "!=",
                        Arrays.asList("x", "xx", "х.х", "х", "хх",
                                "щит неисправен", "снесли", "ушла"),
                        FilterOperator.AND);
        filterChainDescriptionMap.put("badRaitings", badRatingsFilterChain);

    }

    public Set<String> getVipClients() {
        return vipClients;
    }

    public Set<String> getBadBillboardSideRaitings() {
        return badBillboardSideRaitings;
    }

    public Set<String> getIgnoredClients() {
        return ignoredClients;
    }

    public BookingFileInfo getBookingFileInfo() {
        return bookingFile;
    }

    public ColumnMappingInfo getColumnMapping(String property) {
        return Objects.requireNonNull(columnMappings.get(property),
                "no column mapping for property:" + property);
    }

    public OutputColumnMapping getOutputColumnMapping(String property) {
        return Objects.requireNonNull(outputColumnMappings.get(property),
                "no output column mapping for property:" + property);
    }

    public InputColumnMapping getInputColumnMapping(String property) {
        return Objects.requireNonNull(inputColumnMappings.get(property),
                "no input column mapping for property:" + property);
    }

    public Set<ColumnMappingInfo> getProperties(Set<String> properties) {
        return columnMappings.entrySet().stream()
                .filter(property -> properties.contains(property.getKey()))
                .map(e -> e.getValue())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Path getRefferenceFile() {
        return bookingFile.getReferenceFile();
    }

    public List<String> getPropertySet(String setName) {
        return propertySets.get(setName);
    }

    public List<InputColumnMapping> getInputColumnMappings(String... properties) {
        return Arrays.stream(properties)
                .map(this::getInputColumnMapping)
                .collect(Collectors.toList());
    }

    public List<InputColumnMapping> getInputColumnMappings(Collection<String> properties) {
        return properties.stream()
                .map(this::getInputColumnMapping)
                .collect(Collectors.toList());
    }

    public List<InputColumnMapping> getInputColumnMappings() {
        return inputColumnMappings.values().stream().collect(Collectors.toList());
    }

    public List<OutputColumnMapping> getOutputColumnMappings(String... properties) {
        return Arrays.stream(properties)
                .map(this::getOutputColumnMapping)
                .collect(Collectors.toList());
    }
    
    public List<OutputColumnMapping> getOutputColumnMappings(Collection<String> properties) {
        return properties.stream()
                .map(this::getOutputColumnMapping)
                .collect(Collectors.toList());
    }

    public List<OutputColumnMapping> getOutputColumnMappings() {
        return outputColumnMappings.values().stream().collect(Collectors.toList());
    }

    public ColumnStyleInfo getColumnStyleInfo(String property) {
        return outputColumnMappings.get(property).getColumnStyleInfo();
    }

    public Map<String, ColumnStyleInfo> getColumnStyleInfo(List<String> properties) {
        Map<String, ColumnStyleInfo> columnStyleMap = new HashMap<>();
        getOutputColumnMappings(properties).forEach(mapping -> {
            ColumnStyleInfo columnStyleInfo = mapping.getColumnStyleInfo();
            if (columnStyleInfo != null) {
                columnStyleMap.put(mapping.getProperty(), columnStyleInfo);
            }
        });
        return columnStyleMap;
    }

    public CellStyleInfo getBookedSideStyleInfo() {
        return bookedSideStyleInfo;
    }

    public void setBookedSideStyleInfo(
            CellStyleInfo bookedSideStyleInfo) {
        this.bookedSideStyleInfo = bookedSideStyleInfo;
    }

    public CellStyleInfo getFreeSideStyleInfo() {
        return freeSideStyleInfo;
    }

    public void setFreeSideStyleInfo(CellStyleInfo freeSideStyleInfo) {
        this.freeSideStyleInfo = freeSideStyleInfo;
    }

    public ColumnStyleInfo getDefaultColumnStyleInfo() {
        return defaultColumnStyleInfo;
    }

    public void setDefaultColumnStyleInfo(ColumnStyleInfo defaultColumnStyleInfo) {
        this.defaultColumnStyleInfo = defaultColumnStyleInfo;
    }

    public CellStyleInfo getDefaultHeaderStyleInfo() {
        return defaultHeaderStyleInfo;
    }

    public void setDefaultHeaderStyleInfo(CellStyleInfo defaultHeaderStyleInfo) {
        this.defaultHeaderStyleInfo = defaultHeaderStyleInfo;
    }

    public void addSamplePreferences(String name, SamplePreferences samplePreferences) {
        samplePreferencesMap.put(name, samplePreferences);
    }

    public void removeSamplePreferences(String name) {
        samplePreferencesMap.remove(name);
    }

    public static class SamplePreferences {

        private List<String> properties;
        private List<FilterDescriptor> filterDescriptors;

        public SamplePreferences(List<String> properties) {
            this.properties = properties;
        }

        public SamplePreferences(List<String> properties,
                List<FilterDescriptor> filterDescriptors) {
            this.properties = properties;
            this.filterDescriptors = filterDescriptors;
        }

        public List<String> getProperties() {
            return properties;
        }

        public List<FilterDescriptor> getFilterDescriptors() {
            return filterDescriptors;
        }
    }
}
