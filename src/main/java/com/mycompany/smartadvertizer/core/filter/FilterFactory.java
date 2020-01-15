/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core.filter;

/**
 *
 */
public interface FilterFactory<T> {
    Filter<T> createFilter(String operator, T filterValue);    
}
