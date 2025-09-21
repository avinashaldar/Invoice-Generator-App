package com.invoiceapp.model;

public class InvoiceItem {
    private String productName;
    private double unitPrice;
    private int quantity;
    private double gstRate, taxableValue, gstAmount;

    public InvoiceItem(String productName, double unitPrice, int quantity,
                       double gstRate, double taxableValue, double gstAmount) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.gstRate = gstRate;
        this.taxableValue = taxableValue;
        this.gstAmount = gstAmount;
    }

    public String getProductName() { return productName; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public double getGstRate() { return gstRate; }
    public double getTaxableValue() { return taxableValue; }
    public double getGstAmount() { return gstAmount; }
}
