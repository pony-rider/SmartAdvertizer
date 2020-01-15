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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 *
 *
 */
public class XlsReaderTest {

    private static Reader<BillboardSide> reader;

    @BeforeAll
    public static void setUp() {
        ConfigurableConversionService conversionService
                = new DefaultFormattingConversionService();
        conversionService.addConverterFactory(new StringToAliasedEnumConverterFactory());
        conversionService.addConverter(new AliasedEnumToStringConverter());
        conversionService.addConverter(new LocalDateToDateConverter());

        reader = new XlsReader<>(BillboardSide.class, 1);
        reader.setConversionService(conversionService);
    }

    @Test
    public void read() {
        String file = "src/main/resources/xls/общий справочник.xls";
         List<String> properties = Arrays.asList("region", "address", "side",
                "photoLink", "mapLink", "rating", "lighting", "constructionType", "size",
                "price", "firstInstallationPrice",
                "additionalInstallationPrice", "material", "printingPrice", "endOfLicence");
        List<BillboardSide> billboardSides = reader.read(file, properties);
        billboardSides.forEach(System.out::println);
        BillboardSide side = billboardSides.stream().filter(bb -> bb.getAddress().equals(
                "пр. Строителей (ТЦ Проспект)") && bb.getSide().equals("В")).findAny().
                get();
        assertEquals(BillboardSide.Lighting.ON, side.getLighting());
        assertEquals("дальнее арбеково", side.getRegion());
        assertEquals(BillboardSide.ConstuctionType.BILLBOARD, side.getConstructionType());
        assertEquals(side.getMaterial(), BillboardSide.Material.BANNER);
        assertEquals(side.getSize(), BillboardSide.Size.STANDART);
        assertEquals(side.getPrice(), 9000);
        assertEquals(side.getEndOfLicence(), LocalDate.of(2023, 12, 31));
    }

}
