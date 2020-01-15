/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.mapping;

/**
 * 
 */
@Deprecated
public interface InputColumnMapping {
    String getProperty();
    int getColumnIndex();    
    boolean isNullable();    
}
