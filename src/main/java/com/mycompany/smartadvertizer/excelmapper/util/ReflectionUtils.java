/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 *
 */
public class ReflectionUtils {

    public static void setProperty(Object obj, String property, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(property);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() 
                    + " " + property + " " + value, e);
        }
    }

    public static Object getProperty(Object obj, String property) {
        try {
            Field field = obj.getClass().getDeclaredField(property);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> getPropertyType(Class<?> type, String property) {
        try {
            Field field = type.getDeclaredField(property);
            return field.getType();
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
    
     public static <T> void copyProperties(T source, T target, List<String> properties) {
        Class<?> clazz = source.getClass();
        for (String property : properties) {
            try {
                Field field = clazz.getDeclaredField(property);
                field.setAccessible(true);
                Object value = field.get(source);
                field.set(target, value);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException |
                    IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
