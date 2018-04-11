package com.highgate.highgate.adapters;

/**
 * Created by David on 9/22/2017.
 */

public class OrderItemDetail {
    private String stockCode;
    private String description;
    private int quantity;
    private double unitPrice;
    private double lineTotal;

    public OrderItemDetail(){
        this.stockCode = "";
        this.description = "";
        this.quantity = 0;
        this.unitPrice = 0.0;
        this.lineTotal = 0.0;
    }

    public OrderItemDetail(String stockCode, String description, int quantity, double unitPrice, double lineTotal) {
        this.stockCode = stockCode;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }
}
