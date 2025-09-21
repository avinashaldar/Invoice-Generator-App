package com.invoiceapp.util;

import com.invoiceapp.model.Invoice;
import com.invoiceapp.model.InvoiceItem;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PDFGenerator {

    public static void generateInvoicePDF(int invoiceId, Invoice invoice,
                                          List<InvoiceItem> items,
                                          HttpServletResponse resp) throws IOException {

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=invoice_" + invoiceId + ".pdf");

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDPageContentStream cs = new PDPageContentStream(doc, page);

            float margin = 50;
            float yPosition = 750;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float rowHeight = 25;

            // ---------- HEADER ----------
            cs.setFont(PDType1Font.HELVETICA_BOLD, 22);
            cs.beginText();
            cs.newLineAtOffset(margin, yPosition);
            cs.showText("Avinash Pvt. Ltd.");
            cs.endText();

            yPosition -= 30;
            cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
            cs.beginText();
            cs.newLineAtOffset(margin, yPosition);
            cs.showText("INVOICE #" + invoiceId);
            cs.endText();

            // ---------- CUSTOMER INFO ----------
            yPosition -= 40;
            float customerBoxHeight = 80;
            cs.addRect(margin, yPosition - customerBoxHeight, tableWidth, customerBoxHeight);
            cs.stroke();

            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.beginText();
            cs.newLineAtOffset(margin + 5, yPosition - 20);
            cs.showText("Customer Name: " + invoice.getCustomerName());
            cs.newLineAtOffset(0, -15);
            cs.showText("Email: " + invoice.getCustomerEmail());
            cs.newLineAtOffset(0, -15);
            cs.showText("Phone: " + invoice.getCustomerPhone());
            cs.newLineAtOffset(0, -15);
            cs.showText("Address: " + invoice.getCustomerAddress());
            cs.endText();

            // ---------- TABLE ----------
            yPosition -= (customerBoxHeight + 30);
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Column positions (better spacing)
            float[] colX = {
                    margin,           // Product
                    margin + 180,     // Quantity
                    margin + 240,     // Unit Price
                    margin + 320,     // GST
                    margin + 400,     // Taxable
                    margin + 500      // Total
            };
            String[] headers = {"Product", "Quantity", "Unit Price", "GST", "Taxable", "Total"};

            // Header background
            cs.setNonStrokingColor(200, 200, 200);
            cs.addRect(margin, yPosition, tableWidth, rowHeight);
            cs.fill();
            cs.setNonStrokingColor(0);

            // Header text
            for (int i = 0; i < headers.length; i++) {
                cs.beginText();
                cs.newLineAtOffset(colX[i] + 5, yPosition + 7);
                cs.showText(headers[i]);
                cs.endText();
            }

            // Table rows
            yPosition -= rowHeight;
            cs.setFont(PDType1Font.HELVETICA, 12);
            boolean alternate = false;
            for (InvoiceItem item : items) {
                // Alternate shading
                if (alternate) {
                    cs.setNonStrokingColor(240, 240, 240);
                    cs.addRect(margin, yPosition, tableWidth, rowHeight);
                    cs.fill();
                    cs.setNonStrokingColor(0);
                }

                // Row data
                String[] row = {
                        item.getProductName(),
                        String.valueOf(item.getQuantity()),
                        String.format("%.2f", item.getUnitPrice()),
                        String.format("%.2f", item.getGstAmount()),
                        String.format("%.2f", item.getTaxableValue()),
                        String.format("%.2f", item.getTaxableValue() + item.getGstAmount())
                };

                for (int i = 0; i < row.length; i++) {
                    cs.beginText();
                    cs.newLineAtOffset(colX[i] + 5, yPosition + 7);
                    cs.showText(row[i]);
                    cs.endText();
                }

                yPosition -= rowHeight;
                alternate = !alternate;
            }

            // Borders
            float tableTopY = yPosition + rowHeight * (items.size() + 1);
            for (int i = 0; i <= items.size() + 1; i++) {
                cs.moveTo(margin, tableTopY - i * rowHeight);
                cs.lineTo(margin + tableWidth, tableTopY - i * rowHeight);
            }
            for (float x : colX) {
                cs.moveTo(x, tableTopY);
                cs.lineTo(x, yPosition);
            }
            cs.moveTo(margin + tableWidth, tableTopY);
            cs.lineTo(margin + tableWidth, yPosition);
            cs.stroke();

            // ---------- TOTALS ----------
            yPosition -= 30;
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.beginText();
            cs.newLineAtOffset(margin + tableWidth - 160, yPosition);
            cs.showText("Subtotal: " + String.format("%.2f", invoice.getSubtotal()));
            cs.newLineAtOffset(0, -15);
            cs.showText("Tax: " + String.format("%.2f", invoice.getTotalTax()));
            cs.newLineAtOffset(0, -15);
            cs.showText("Grand Total: " + String.format("%.2f", invoice.getTotalAmount()));
            cs.endText();

            cs.close();
            doc.save(resp.getOutputStream());
        }
    }
}
