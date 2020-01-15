/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.smartadvertizer.excelmapper;

import java.util.List;

/**
 *
 */
public interface AppendableWriter<T> extends Writer<T>{
    void append(String file, List<T> entities, List<String> properties, int startColumn);
}
