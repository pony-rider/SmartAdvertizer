/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import com.mycompany.smartadvertizer.core.BillboardSideReaderTest.TestConfig;
import com.mycompany.smartadvertizer.core.config.AppConfig;
import com.mycompany.smartadvertizer.core.filter.FilterChainFactory;
import com.mycompany.smartadvertizer.excelmapper.convert.AliasedEnumToStringConverter;
import com.mycompany.smartadvertizer.excelmapper.convert.LocalDateToDateConverter;
import com.mycompany.smartadvertizer.excelmapper.convert.StringToAliasedEnumConverterFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class BillboardSideReaderTest {
    
    @Configuration
    @ComponentScan("com.mycompany.smartadvertizer.core")
    public static class TestConfig {}
    
    
    @Autowired
    private static FilterChainFactory filterChainFactory;
    private static BillboardSideReader reader;

    @BeforeAll
    public static void setUp() {
        ConfigurableConversionService conversionService
                = new DefaultFormattingConversionService();
        conversionService.addConverterFactory(new StringToAliasedEnumConverterFactory());
        conversionService.addConverter(new AliasedEnumToStringConverter());
        conversionService.addConverter(new LocalDateToDateConverter());
        Config config = new Config();
        reader = new BillboardSideReader(config, conversionService, filterChainFactory);

    }

    @Test
    public void getFreeBillboards() {
        List<String> inputProperties = Arrays.asList("region", "address", "side",
                "rating", "lighting",
                "constructionType", "size", "price", "firstInstallationPrice",
                "additionalInstallationPrice", "material", "printingPrice");
        List<BillboardSide> billboardSides = reader.getFreeBillboardSides(
                YearMonth.of(2019, 12), YearMonth.of(2019, 12), inputProperties);
        List<String> streets = billboardSides.stream().map(bb -> bb.getAddress())
                .collect(Collectors.toList());
        //billboardSides.forEach(System.out::println);
        List<String> streetsToCheck = Arrays.asList(
                "пр. Строителей (ТЦ Проспект)",
                "пр. Строителей 146",
                "пр. Строителей - ул. Стасова (рядом с АЗС ЮКОС)",
                "пр. Строителей(ост.Колледж Управления)",
                "пр. Строителей 22",
                "ул. Суворова 14а (Кулакова)");
        assertEquals(streets.size(), streets.size());
        assertEquals(streetsToCheck, streets);
    }
    
    @Test
    public void readBillboardSides() {
        String file = "src/test/resources/xls/адресная.xls";
        List<BillboardSide> billboardSides = reader.readBillboardSides(file, "B", "C");
        List<BillboardSide> expectedBillboardSides = Arrays.asList(
                new BillboardSide("пр. Строителей (ТЦ Проспект)", "В"),
                new BillboardSide("пр. Строителей 146", "В"),
                new BillboardSide("пр. Строителей - ул. Стасова (рядом с АЗС ЮКОС)", "А1"),
                new BillboardSide("пр. Строителей(ост.Колледж Управления)", "В"),
                new BillboardSide("пр. Строителей 22", "В"),
                new BillboardSide("ул. Суворова 14а (Кулакова)", "В")
        );
        //System.out.println(billboardSides);
        //System.out.println(expectedBillboardSides);
        assertEquals(expectedBillboardSides, billboardSides);
    }

    @Test
    public void findRowToSkip() throws IOException {
        Path filePath = Paths.get("src/test/resources/xls/адресная.xls");
        try (Workbook book = new HSSFWorkbook(Files.newInputStream(filePath))) {
            int rowsToSkipp = reader.findRowsToSkip(book.getSheetAt(0));
            assertEquals(2, rowsToSkipp);
        }
    }
}
