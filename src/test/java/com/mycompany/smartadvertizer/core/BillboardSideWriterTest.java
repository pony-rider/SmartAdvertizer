/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import com.mycompany.smartadvertizer.core.BillboardSideWriterTest.TestConfig;
import com.mycompany.smartadvertizer.core.config.AppConfig;
import com.mycompany.smartadvertizer.core.filter.FilterChainFactory;
import com.mycompany.smartadvertizer.core.filter.FilterChainTest;
import com.mycompany.smartadvertizer.excelmapper.convert.AliasedEnumToStringConverter;
import com.mycompany.smartadvertizer.excelmapper.convert.LocalDateToDateConverter;
import com.mycompany.smartadvertizer.excelmapper.convert.StringToAliasedEnumConverterFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 *
 */
@SpringBootTest(classes = {AppConfig.class})
public class BillboardSideWriterTest {

    @Configuration
    @ComponentScan("com.mycompany.smartadvertizer.core")
    public static class TestConfig {}
    
    @Autowired
    private static FilterChainFactory filterChainFactory;
    private static BillboardSideReader reader;
    private static BillboardSideWriter writer;

    @BeforeAll
    public static void setUp() {
        ConfigurableConversionService conversionService
                = new DefaultFormattingConversionService();
        conversionService.addConverterFactory(new StringToAliasedEnumConverterFactory());
        conversionService.addConverter(new AliasedEnumToStringConverter());
        conversionService.addConverter(new LocalDateToDateConverter());
        Config config = new Config();
        reader = new BillboardSideReader(config, conversionService, filterChainFactory);
        writer = new BillboardSideWriter(config, conversionService);
    }

    @Test
    public void writeSampleWithBookingStatuses() throws IOException {
        List<String> inputProperties = Arrays.asList("region", "address", "side",
                "rating", "lighting",
                "constructionType", "size", "price", "firstInstallationPrice",
                "additionalInstallationPrice", "material", "printingPrice");
        YearMonth start = YearMonth.of(2019, 9);
        YearMonth end = YearMonth.of(2019, 12);
        List<BillboardSide> billboardSides = reader.getFreeBillboardSides(
                start, end, inputProperties);
        String outputFile = "src/test/resources/billboardSideWriter-test.xls";
        Path outputPath = Paths.get(outputFile);
        Files.deleteIfExists(outputPath);
        Files.createFile(outputPath);
        List<String> outputProperties = Arrays.asList("region", "address", "side",
                "rating", "bookingStatuses", "lighting",
                "constructionType", "size", "price", "firstInstallationPrice",
                "additionalInstallationPrice", "material", "printingPrice");
        writer.write(outputFile, new Sample(TimeUtil.getMonthsNames(start, end),
                billboardSides, outputProperties));
    }
}
