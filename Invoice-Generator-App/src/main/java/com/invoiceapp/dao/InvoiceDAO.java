package com.invoiceapp.dao;

import com.invoiceapp.model.Invoice;
import com.invoiceapp.model.InvoiceItem;
import com.invoiceapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    public int saveInvoice(Invoice invoice, List<InvoiceItem> items) {
        int invoiceId = -1;
        String sqlInvoice = "INSERT INTO invoices (created_by, customer_name, customer_email, " +
                "customer_phone, customer_address, subtotal, total_tax, total_amount) VALUES (?,?,?,?,?,?,?,?)";

        String sqlItem = "INSERT INTO invoice_items (invoice_id, product_name, unit_price, quantity, gst_rate, taxable_value, gst_amount) " +
                "VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlInvoice, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, invoice.getCreatedBy());
                ps.setString(2, invoice.getCustomerName());
                ps.setString(3, invoice.getCustomerEmail());
                ps.setString(4, invoice.getCustomerPhone());
                ps.setString(5, invoice.getCustomerAddress());
                ps.setDouble(6, invoice.getSubtotal());
                ps.setDouble(7, invoice.getTotalTax());
                ps.setDouble(8, invoice.getTotalAmount());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    invoiceId = rs.getInt(1);
                }
            }

            try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                for (InvoiceItem item : items) {
                    psItem.setInt(1, invoiceId);
                    psItem.setString(2, item.getProductName());
                    psItem.setDouble(3, item.getUnitPrice());
                    psItem.setInt(4, item.getQuantity());
                    psItem.setDouble(5, item.getGstRate());
                    psItem.setDouble(6, item.getTaxableValue());
                    psItem.setDouble(7, item.getGstAmount());
                    psItem.addBatch();
                }
                psItem.executeBatch();
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceId;
    }
    
    public List<Invoice> getInvoicesByUser(int userId) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT id, customer_name, customer_email, customer_phone, customer_address, " +
                     "subtotal, total_tax, total_amount, created_at " +
                     "FROM invoices WHERE created_by = ? ORDER BY created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Invoice inv = new Invoice(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("customer_email"),
                        rs.getString("customer_phone"),
                        rs.getString("customer_address"),
                        userId,
                        rs.getDouble("subtotal"),
                        rs.getDouble("total_tax"),
                        rs.getDouble("total_amount"),
                        rs.getTimestamp("created_at")
                );
                invoices.add(inv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoices;
    }
    
 // Existing methods (saveInvoice, getInvoicesByUser) here...

    // New method to fetch items for a specific invoice
    public List<InvoiceItem> getInvoiceItems(int invoiceId) {
        List<InvoiceItem> items = new ArrayList<>();
        String sql = "SELECT product_name, unit_price, quantity, gst_rate, taxable_value, gst_amount " +
                     "FROM invoice_items WHERE invoice_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoiceId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InvoiceItem item = new InvoiceItem(
                        rs.getString("product_name"),
                        rs.getDouble("unit_price"),
                        rs.getInt("quantity"),
                        rs.getDouble("gst_rate"),
                        rs.getDouble("taxable_value"),
                        rs.getDouble("gst_amount")
                );
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public Invoice getInvoiceById(int invoiceId) {
        Invoice invoice = null;
        String sql = "SELECT id, created_by, customer_name, customer_email, customer_phone, customer_address, subtotal, total_tax, total_amount, created_at " +
                     "FROM invoices WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoiceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                invoice = new Invoice(
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    rs.getString("customer_email"),
                    rs.getString("customer_phone"),
                    rs.getString("customer_address"),
                    rs.getInt("created_by"),
                    rs.getDouble("subtotal"),
                    rs.getDouble("total_tax"),
                    rs.getDouble("total_amount"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoice;
    }



}
