/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.smartadvertizer.excelmapper;

/**
 *
 */
public interface ReaderFactory {
    <T> Reader<T> createReader(Class<T> type, int skipRows);
    <T> Reader<T> createReader(Class<T> type);
}
