/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.mapping.annotation;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 *
 * @author user
 */
public @interface ColumnStyle {
    boolean bordered() default false;
    boolean wrapText() default false;
    HorizontalAlignment alignment() default HorizontalAlignment.GENERAL;
    short color() default -1;
    short textRotation() default 0;
    boolean autoWidth() default false;
    int width() default -1;    
}
