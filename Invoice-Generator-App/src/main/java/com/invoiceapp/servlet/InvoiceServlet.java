package com.invoiceapp.servlet;

import com.invoiceapp.dao.InvoiceDAO;
import com.invoiceapp.model.Invoice;
import com.invoiceapp.model.InvoiceItem;
import com.invoiceapp.model.User;
import com.invoiceapp.util.PDFGenerator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/createInvoice")
public class InvoiceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get action (save or download)
        String action = req.getParameter("action");

        // Get logged-in user from session
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int createdBy = user.getId();

        // Collect invoice details
        String customerName = req.getParameter("customer_name");
        String customerEmail = req.getParameter("customer_email");
        String customerPhone = req.getParameter("customer_phone");
        String customerAddress = req.getParameter("customer_address");

        double subtotal = Double.parseDouble(req.getParameter("subtotal"));
        double totalTax = Double.parseDouble(req.getParameter("total_tax"));
        double totalAmount = Double.parseDouble(req.getParameter("total_amount"));

        String[] productNames = req.getParameterValues("product_name");
        String[] unitPrices = req.getParameterValues("unit_price");
        String[] quantities = req.getParameterValues("quantity");
        String[] gstRates = req.getParameterValues("gst_rate");
        String[] taxableValues = req.getParameterValues("taxable_value");
        String[] gstAmounts = req.getParameterValues("gst_amount");

        Invoice invoice = new Invoice(customerName, customerEmail, customerPhone,
                customerAddress, createdBy, subtotal, totalTax, totalAmount);

        List<InvoiceItem> items = new ArrayList<>();
        for (int i = 0; i < productNames.length; i++) {
            items.add(new InvoiceItem(
                    productNames[i],
                    Double.parseDouble(unitPrices[i]),
                    Integer.parseInt(quantities[i]),
                    Double.parseDouble(gstRates[i]),
                    Double.parseDouble(taxableValues[i]),
                    Double.parseDouble(gstAmounts[i])
            ));
        }

        // Save invoice
        InvoiceDAO dao = new InvoiceDAO();
        int invoiceId = dao.saveInvoice(invoice, items);

        if (invoiceId > 0) {
            if ("save".equals(action)) {
                // Just save and go to dashboard
                resp.sendRedirect("dashboard");
            } else if ("download".equals(action)) {
                // Generate and download PDF
                PDFGenerator.generateInvoicePDF(invoiceId, invoice, items, resp);
            } else {
                // Default fallback
                resp.sendRedirect("dashboard");
            }
        } else {
            resp.sendRedirect("jsp/error.jsp");
        }
    }
}
