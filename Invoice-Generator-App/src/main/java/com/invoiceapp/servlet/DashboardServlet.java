package com.invoiceapp.servlet;

import com.invoiceapp.dao.InvoiceDAO;
import com.invoiceapp.model.Invoice;
import com.invoiceapp.model.InvoiceItem;
import com.invoiceapp.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int userId = user.getId();  // logged-in user ID

        InvoiceDAO dao = new InvoiceDAO();
        List<Invoice> invoices = dao.getInvoicesByUser(userId);

        // **Fetch items for each invoice**
        for (Invoice inv : invoices) {
            List<InvoiceItem> items = dao.getInvoiceItems(inv.getId());
            inv.setItems(items); // make sure Invoice has setItems(List<InvoiceItem>) method
        }

        req.setAttribute("invoices", invoices);
        RequestDispatcher rd = req.getRequestDispatcher("dashboard.jsp");
        rd.forward(req, resp);
    }
}
