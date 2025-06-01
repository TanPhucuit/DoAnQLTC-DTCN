package com.personal.finance.testproject.service;

import java.math.BigDecimal;

public class BingXSpotTicker {
    private String symbol;
    private BigDecimal lastPrice;

    public BingXSpotTicker(String symbol, BigDecimal lastPrice) {
        this.symbol = symbol;
        this.lastPrice = lastPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }
} 