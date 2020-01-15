package com.mycompany.smartadvertizer.core;

import com.mycompany.smartadvertizer.core.config.AppConfig;
import com.mycompany.smartadvertizer.core.filter.FilterChainDescription;
import com.mycompany.smartadvertizer.core.filter.FilterDescriptor;
import com.mycompany.smartadvertizer.core.filter.FilterOperator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 *
 */
@SpringBootApplication
@Import(AppConfig.class)
public class Main implements CommandLineRunner {

    @Autowired
    private BillboardManager billboardManager;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        String outputFile = "src/main/resources/запросы/адресная.xls";
        Path path = Paths.get(outputFile);
        Files.deleteIfExists(path);
        Files.createFile(path);
        FilterDescriptor addressFilter = new FilterDescriptor("address", "c", "Строителей");
        FilterDescriptor worstRaitingsFilter = new FilterDescriptor("rating", "!=", "xx");

        List<String> inputProperties = Arrays.asList("region", "address", "side",
                "rating", "lighting",
                "constructionType", "size", "price", "firstInstallationPrice",
                "additionalInstallationPrice", "material", "printingPrice", "endOfLicence");
        List<String> outputProperties = Arrays.asList("region", "address", "side",
                "bookingStatuses", "lighting",
                "constructionType", "size", "price", "firstInstallationPrice",
                "additionalInstallationPrice", "material", "printingPrice", "endOfLicence");

        FilterChainDescription filterChainDescription = new FilterChainDescription(
                worstRaitingsFilter).add(FilterOperator.AND, addressFilter);
        Query query = Query.from(inputProperties, outputProperties,
                YearMonth.of(2019, 12), YearMonth.of(2020, 2),
                filterChainDescription);
        billboardManager.createAndSaveSample(query, outputFile);
    }
}
