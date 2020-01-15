/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class FileUtil {

    public static void checkFileExists(String file) {
        Path filePath = Paths.get(file);
        if (Files.notExists(filePath)) {
            throw new RuntimeException(new FileNotFoundException(
                    "specified path does not exist: " + filePath));
        }
        if (!Files.isRegularFile(filePath)) {
            throw new RuntimeException(new FileNotFoundException(
                    "specified path is not regular file: " + filePath));
        }
    }
    
    public static void checkNotFileExists(String file) {
        Path filePath = Paths.get(file);
        if (Files.exists(filePath)) {
            throw new RuntimeException(new FileNotFoundException(
                    "specified path already exists: " + filePath));
        }
    }

    public static void checkDirExists(Path dir) {
        if (Files.notExists(dir)) {
            throw new RuntimeException(new FileNotFoundException(
                    "specified path does not exist: " + dir));
        }
        if (!Files.isDirectory(dir)) {
            throw new RuntimeException(new FileNotFoundException(
                    "specified path is not a directory: " + dir));
        }
    }

    public static List<String> copyFiles(String srcDir, String destDir, List<String> files) {
        Path srcDirPath = Paths.get(srcDir);
        Path destDirPath = Paths.get(destDir);
        return copyFiles(srcDirPath, destDirPath, files);
    }

    public static List<String> copyFiles(Path srcDir, Path destDir, List<String> files) {
        List<String> notExistingFiles = new ArrayList<>();
        for (String file : files) {
            Path srcFile = srcDir.resolve(file);
            Path destFile = destDir.resolve(file);
            if (Files.exists(srcFile)) {
                try {
                    Files.copy(srcFile, destFile);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                notExistingFiles.add(file);
            }
        }
        return notExistingFiles;
    }
}
