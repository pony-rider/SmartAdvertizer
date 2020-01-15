/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper;

import com.mycompany.smartadvertizer.core.BillboardSide;
import com.mycompany.smartadvertizer.core.Config;
import com.mycompany.smartadvertizer.excelmapper.convert.AliasedEnumToStringConverter;
import com.mycompany.smartadvertizer.excelmapper.convert.LocalDateToDateConverter;
import com.mycompany.smartadvertizer.excelmapper.convert.StringToAliasedEnumConverterFactory;
import com.mycompany.smartadvertizer.excelmapper.style.CellStyleInfo;
import com.mycompany.smartadvertizer.excelmapper.style.ColumnStyleInfo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 *
 *
 */
public class XlsWriterTest {

    private static Reader<BillboardSide> reader;
    private static Writer<BillboardSide> writer;

    @BeforeAll
    public static void setUp() {
        ConfigurableConversionService conversionService
                = new DefaultFormattingConversionService();
        conversionService.addConverterFactory(new StringToAliasedEnumConverterFactory());
        conversionService.addConverter(new AliasedEnumToStringConverter());
        conversionService.addConverter(new LocalDateToDateConverter());

        reader = new XlsReader<>(BillboardSide.class, 1);
        reader.setConversionService(conversionService);

        writer = new XlsWriter<>(BillboardSide.class);
        writer.setCreateHeadersRow(true);
        writer.setConversionService(conversionService);
        ColumnStyleInfo defaultColumnStyleInfo = new ColumnStyleInfo(true)
                .setAutoWidth(true);
        defaultColumnStyleInfo.setAlignment(HorizontalAlignment.CENTER);
        CellStyleInfo defaultHeaderStyleInfo = new CellStyleInfo(true, true)
                .setAlignment(HorizontalAlignment.CENTER)
                .setColor(IndexedColors.PALE_BLUE.getIndex());

        writer.setDefaultHeaderStyleInfo(defaultHeaderStyleInfo);
        writer.setDefaultColumnStyleInfo(defaultColumnStyleInfo);
    }

    @Test
    public void saveToXls() throws IOException {
        String file = "src/main/resources/xls/общий справочник.xls";
        List<String> properties = Arrays.asList("region", "address", "side",
                "photoLink", "mapLink", "rating", "lighting", "constructionType", "size",
                "price", "firstInstallationPrice",
                "additionalInstallationPrice", "material", "printingPrice", "endOfLicence");
        List<BillboardSide> billboardSides = reader.read(file, properties);

        List<String> outputProperties = Arrays.asList("address", "side", "rating",
                "lighting", "price", "photoLink", "mapLink");

        String outputFile = "src/test/resources/writer-test.xls";
        Path outputPath = Paths.get(outputFile);
        Files.deleteIfExists(outputPath);
        Files.createFile(outputPath);
        writer.write(outputFile, billboardSides, outputProperties);
    }

}
