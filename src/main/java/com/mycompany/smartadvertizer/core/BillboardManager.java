/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import com.mycompany.smartadvertizer.core.filter.FilterChainFactory;
import com.mycompany.smartadvertizer.excelmapper.util.FileUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class BillboardManager {

    private Config config;
    private BillboardSideReader reader;
    private BillboardSideWriter writer;

    @Autowired
    public BillboardManager(Config config, ConversionService conversionService,
            FilterChainFactory filterChainFactory) {
        this.config = config;
        reader = new BillboardSideReader(config, conversionService, filterChainFactory);
        writer = new BillboardSideWriter(config, conversionService);
    }

    public void createAndSaveSample(Query query, String outputFile) {
        Sample sample = createSample(query);
        writer.write(outputFile, sample);
    }

    public Sample createSample(Query query) {
        List<BillboardSide> billboardSides;
        if (query.getFilterChainDescription() != null) {
            billboardSides = reader.getFreeBillboardSides(query.getYearMonths(),
                    query.getInputProperties(), query.getFilterChainDescription());
        } else {
            billboardSides = reader.getFreeBillboardSides(query.getYearMonths(),
                    query.getInputProperties());
        }
        return new Sample(TimeUtil.getMonthsNames(query.getYearMonths()), billboardSides,
                query.getOutputProperties());
    }

    public FilesCollectingResult collectPhoto(String file, String destDir,
            String adrrColumn, String sideColumn) throws IOException {
        FileUtil.checkFileExists(file);
        List<BillboardSide> billboardSides = reader.readBillboardSides(file, adrrColumn,
                sideColumn);
        return collectPhoto(billboardSides, destDir);
    }

    public FilesCollectingResult collectPhoto(List<BillboardSide> billboardSides,
            String destDir) {
        Path destDirPath = Paths.get(destDir);
        try {
            Files.createDirectory(destDirPath);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        reader.fillWithPropertiesFromReference(billboardSides, Arrays.asList("photo"));
        List<BillboardSide> billboardSidesWithNullFiles = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        billboardSides.stream().forEach(bb -> {
            String file = bb.getPhoto();
            if (file == null || file.isEmpty()) {
                billboardSidesWithNullFiles.add(bb);
            } else {
                fileNames.add(file);
            }
        });
        List<String> filesNotFound = FileUtil.copyFiles(config.getBookingFileInfo().
                getPhotoDir(), destDirPath, fileNames);
        int filesFound = fileNames.size() - filesNotFound.size();
        return new FilesCollectingResult(filesFound, billboardSidesWithNullFiles,
                filesNotFound);
    }

    public static class FilesCollectingResult {

        private final int filesFound;
        private final List<BillboardSide> billboardSidesWithNullFiles;
        private final List<String> filesNotFoundList;

        private FilesCollectingResult(int filesFound,
                List<BillboardSide> billboardSidesWithNullFiles,
                List<String> filesNotFoundList) {
            this.filesFound = filesFound;
            this.billboardSidesWithNullFiles = billboardSidesWithNullFiles;
            this.filesNotFoundList = filesNotFoundList;
        }

        public List<BillboardSide> getBillboardSidesWithNullFiles() {
            return billboardSidesWithNullFiles;
        }

        public List<String> getFilesNotFoundList() {
            return filesNotFoundList;
        }

        public int getFilesFound() {
            return filesFound;
        }

        public int getFilesNotFound() {
            return filesNotFoundList.size();
        }
    }
}
