/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import java.util.List;
import java.util.Objects;

/**
 *
 */
public class Sample {

    private final List<String> months;
    private final List<BillboardSide> billboardSides;
    private final List<String> outputProperties;

    public Sample(List<String> months, List<BillboardSide> billboardSides,
            List<String> outputProperties) {
        this.months = Objects.requireNonNull(months, "months must be not null");
        this.billboardSides = Objects.requireNonNull(billboardSides,
                "billboardSide must be not null");
        this.outputProperties = Objects.requireNonNull(outputProperties,
                "outputProperties must be not null");
    }

    public List<String> getMonths() {
        return months;
    }

    public List<BillboardSide> getBillboardSides() {
        return billboardSides;
    }

    public List<String> getOutputProperties() {
        return outputProperties;
    }
}
