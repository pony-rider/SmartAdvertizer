/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserConfig {
    String path = System.getProperty("user.dir") + "\\test-data\\";
    Path destFolder = Paths.get(path + "\\запросы");    
}
