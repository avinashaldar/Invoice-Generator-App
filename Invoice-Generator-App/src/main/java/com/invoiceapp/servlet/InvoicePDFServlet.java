package com.invoiceapp.servlet;

import com.invoiceapp.dao.InvoiceDAO;
import com.invoiceapp.model.Invoice;
import com.invoiceapp.model.InvoiceItem;
import com.invoiceapp.util.PDFGenerator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/invoicePDF")
public class InvoicePDFServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect("dashboard");
            return;
        }

        int invoiceId;
        try {
            invoiceId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect("dashboard");
            return;
        }

        InvoiceDAO dao = new InvoiceDAO();

        // Fetch invoice
        Invoice invoice = dao.getInvoiceById(invoiceId);

        if (invoice == null) {
            resp.sendRedirect("dashboard");
            return;
        }

        // Fetch items for this invoice
        List<InvoiceItem> items = dao.getInvoiceItems(invoiceId);

        if (items == null || items.isEmpty()) {
            resp.sendRedirect("dashboard");
            return;
        }

        // Generate PDF
        PDFGenerator.generateInvoicePDF(invoiceId, invoice, items, resp);
    }
}
