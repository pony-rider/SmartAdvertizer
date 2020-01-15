/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper;

import com.mycompany.smartadvertizer.core.BillboardSide;
import com.mycompany.smartadvertizer.excelmapper.convert.AliasedEnumToStringConverter;
import com.mycompany.smartadvertizer.excelmapper.convert.LocalDateToDateConverter;
import com.mycompany.smartadvertizer.excelmapper.convert.StringToAliasedEnumConverterFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 *
 */
public class AppendableXlsWriterTest {

    private static Reader<BillboardSide> reader;
    private static AppendableWriter<BillboardSide> writer;

    @BeforeAll
    public static void setUp() {
        ConfigurableConversionService conversionService
                = new DefaultFormattingConversionService();
        conversionService.addConverterFactory(new StringToAliasedEnumConverterFactory());
        conversionService.addConverter(new AliasedEnumToStringConverter());
        conversionService.addConverter(new LocalDateToDateConverter());

        reader = new XlsReader<>(BillboardSide.class, 1);
        reader.setConversionService(conversionService);

        writer = new AppendableXlsWriter<>(BillboardSide.class);
        writer.setCreateHeadersRow(true);
        writer.setConversionService(conversionService);
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

        String outputFile = "src/test/resources/appendableWriter-test.xls";
        Path outputPath = Paths.get(outputFile);
        Files.deleteIfExists(outputPath);
        Files.createFile(outputPath);

        int splitIndex = 2;
        List<String> outputPropertiesPartOne = outputProperties.subList(0, splitIndex);
        List<String> outputPropertiesPartTwo = outputProperties.subList(splitIndex,
                outputProperties.size());
        writer.write(outputFile, billboardSides, outputPropertiesPartOne);
        writer.append(outputFile, billboardSides, outputPropertiesPartTwo, splitIndex);
    }

}
