/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.mapping.annotation;

import com.mycompany.smartadvertizer.excelmapper.mapping.ExcelType;
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
public @interface OutputColumn {
    String header();
    ExcelType excelType() default ExcelType.STRING;
    String hyperlinkText() default "link";    
    String dateFormat() default "dd.MM.yy";   
    ColumnStyle style() default @ColumnStyle;
}
