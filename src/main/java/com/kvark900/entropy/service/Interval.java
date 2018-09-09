package com.kvark900.entropy.service;

import java.math.BigDecimal;

public class Interval {
    private BigDecimal lowerBound;
    private BigDecimal upperBound;

    public Interval(BigDecimal lowerBound, BigDecimal upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public Interval() {
    }

    public boolean contains(BigDecimal number) {
        return (number.compareTo(lowerBound) >= 0 && number.compareTo(upperBound) <= 0);
    }

    public boolean containsWithinBoundaries(BigDecimal number) {
        return (number.compareTo(lowerBound) > 0 && number.compareTo(upperBound) < 0);
    }

    public BigDecimal getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(BigDecimal lowerBound) {
        this.lowerBound = lowerBound;
    }

    public BigDecimal getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(BigDecimal upperBound) {
        this.upperBound = upperBound;
    }

}