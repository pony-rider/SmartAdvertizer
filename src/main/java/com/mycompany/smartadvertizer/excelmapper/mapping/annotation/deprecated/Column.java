/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.mapping.annotation.deprecated;

import com.mycompany.smartadvertizer.excelmapper.mapping.ExcelType;
import com.mycompany.smartadvertizer.excelmapper.mapping.annotation.ColumnStyle;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author user
 */
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String index();
    boolean nullable() default true;
    String header();
    ExcelType excelType() default ExcelType.STRING;
    String hyperlinkText() default "";    
    String dateFormat() default "dd.MM.yy";      
    ColumnStyle style() default @ColumnStyle; 
}
