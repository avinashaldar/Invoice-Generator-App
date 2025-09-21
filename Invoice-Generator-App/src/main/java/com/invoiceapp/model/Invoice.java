package com.invoiceapp.model;

import java.sql.Timestamp;
import java.util.List;
import com.invoiceapp.model.InvoiceItem;

public class Invoice {
    private int id;
    private int createdBy;
    private String customerName, customerEmail, customerPhone, customerAddress;
    private double subtotal, totalTax, totalAmount;
    private Timestamp createdAt; // timestamp from DB

    // **New property to store items of this invoice**
    private List<InvoiceItem> items;

    // Full constructor
    public Invoice(int id, String customerName, String customerEmail, String customerPhone,
                   String customerAddress, int createdBy,
                   double subtotal, double totalTax, double totalAmount,
                   Timestamp createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.createdBy = createdBy;
        this.subtotal = subtotal;
        this.totalTax = totalTax;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    // For inserting (no id/createdAt yet)
    public Invoice(String customerName, String customerEmail, String customerPhone,
                   String customerAddress, int createdBy,
                   double subtotal, double totalTax, double totalAmount) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.createdBy = createdBy;
        this.subtotal = subtotal;
        this.totalTax = totalTax;
        this.totalAmount = totalAmount;
    }

    // Getters
    public int getId() { return id; }
    public int getCreatedBy() { return createdBy; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public String getCustomerAddress() { return customerAddress; }
    public double getSubtotal() { return subtotal; }
    public double getTotalTax() { return totalTax; }
    public double getTotalAmount() { return totalAmount; }
    public Timestamp getCreatedAt() { return createdAt; }

    // **Getter & Setter for items**
    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }
}
