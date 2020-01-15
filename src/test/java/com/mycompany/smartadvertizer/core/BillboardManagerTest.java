/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import com.mycompany.smartadvertizer.core.filter.FilterDescriptor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import static com.mycompany.smartadvertizer.core.BillboardManager.FilesCollectingResult;
import com.mycompany.smartadvertizer.core.config.AppConfig;
import com.mycompany.smartadvertizer.core.filter.FilterChainDescription;
import com.mycompany.smartadvertizer.core.filter.FilterOperator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 *
 */
@SpringBootTest(classes = {AppConfig.class})
public class BillboardManagerTest {

    @Autowired
    private BillboardManager billboardManager;

    @Test
    public void createAndSaveSample() throws IOException {
        String outputFile = "src/test/resources/billboardManager-test.xls";
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
                YearMonth.of(2019, 12), YearMonth.of(2019, 12),
                filterChainDescription);
        billboardManager.createAndSaveSample(query, outputFile);
    }

    @Test
    public void collectPhoto() throws IOException {
        String file = "src/test/resources/xls/адресная.xls";
        String destDir = "src/test/resources/фото адресная";
        Path photoDir = Paths.get(destDir);
        Files.deleteIfExists(photoDir);
        FilesCollectingResult collectingResult = billboardManager.collectPhoto(file,
                destDir, "B", "C");
        assertTrue(collectingResult.getBillboardSidesWithNullFiles().isEmpty());
        assertTrue(collectingResult.getFilesNotFoundList().isEmpty());
        int filesCount = new File(destDir).list().length;
        assertEquals(6, filesCount);
        deleteDir(photoDir);
    }

    private List<String> getFileNames(Path dir) throws IOException {
        return Files.walk(dir).filter(Files::isRegularFile)
                .map(x -> x.toString()).collect(Collectors.toList());
    }

    private void deleteDir(Path dir) throws IOException {
        if (Files.exists(dir)) {
            Files.walk(dir).sorted(Comparator.reverseOrder()).forEach((path) -> {
                try {
                    Files.delete(path);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }
}
