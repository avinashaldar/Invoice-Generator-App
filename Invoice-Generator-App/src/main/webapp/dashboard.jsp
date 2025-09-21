<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.invoiceapp.model.User" %>
<%@ page import="com.invoiceapp.model.Invoice" %>
<%@ page import="com.invoiceapp.model.InvoiceItem" %>
<%@ page import="java.util.List" %>
<%@ page session="true" %>
<%
User user = (User) session.getAttribute("user");
if (user == null) {
    response.sendRedirect("login.jsp");
    return;
}

// Retrieve the invoices list set by DashboardServlet
List<Invoice> invoices = (List<Invoice>) request.getAttribute("invoices");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fb; font-family: 'Segoe UI', sans-serif; }
        .welcome-section h1 { font-weight: 600; }
        .welcome-section p { font-size: 0.95rem; }
        .card { border-radius: 12px; }
        .btn-pdf { background-color: #0d6efd; color: #fff; border-radius: 6px; }
        .btn-pdf:hover { background-color: #0b5ed7; }
        .product-list { padding-left: 1rem; margin: 0; list-style-type: disc; font-size: 0.9rem; }
        .table thead th { vertical-align: middle; text-align: center; }
        .table tbody td { vertical-align: middle; padding: 12px; font-size: 0.92rem; }
        .table tbody td ul { margin: 0; padding-left: 1rem; text-align: left; }
        .table-striped > tbody > tr:nth-of-type(odd) { background-color: #f9f9f9; }
        .invoice-card { transition: transform 0.2s, box-shadow 0.2s; }
        .invoice-card:hover { transform: translateY(-4px); box-shadow: 0 8px 18px rgba(0,0,0,0.1); }
    </style>
</head>
<body>
<div class="container mt-5">

    <!-- Welcome Section -->
    <div class="welcome-section text-center mb-4">
        <h1 class="mb-2">Welcome, <%= user.getName() %> 👋</h1>
        <p class="text-muted">Email: <%= user.getEmail() %> | Phone: <%= user.getPhone() %></p>
        <a href="logout.jsp" class="btn btn-danger btn-sm mt-2">Logout</a>
    </div>

    <!-- Generate Invoice Card -->
    <div class="row mb-4">
        <div class="col-md-4 mx-auto">
            <div class="card invoice-card text-center shadow-sm p-4">
                <a href="invoice.jsp" class="btn btn-success btn-lg">
                    🧾 Generate Invoice
                </a>
            </div>
        </div>
    </div>

    <!-- Invoices Table -->
    <div class="card shadow-sm p-4">
        <h4 class="mb-3 fw-semibold">📑 My Transactions</h4>
        <div class="table-responsive">
            <table class="table table-bordered table-hover table-striped align-middle">
                <thead class="table-dark">
                    <tr>
                        <th>#</th>
                        <th>Invoice ID</th>
                        <th>Customer Name</th>
                        <th>Phone No</th>
                        <th>Products</th>
                        <th>Price (₹)</th>
                        <th>Qty</th>
                        <th>GST (₹)</th>
                        <th>Subtotal (₹)</th>
                        <th>Total Amount (₹)</th>
                        <th>Date</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int count = 1;
                        if (invoices != null && !invoices.isEmpty()) {
                            for (Invoice inv : invoices) {
                                List<InvoiceItem> items = inv.getItems();
                    %>
                    <tr>
                        <td class="text-center"><%= count++ %></td>
                        <td class="text-center fw-bold"><%= inv.getId() %></td>
                        <td class="text-start"><%= inv.getCustomerName() %></td>
                        <td class="text-center"><%= inv.getCustomerPhone() %></td>

                        <!-- Product names -->
                        <td>
                            <ul class="product-list">
                                <%
                                    for (InvoiceItem item : items) {
                                %>
                                <li><%= item.getProductName() %></li>
                                <%
                                    }
                                %>
                            </ul>
                        </td>

                        <!-- Unit Prices -->
                        <td>
                            <ul class="product-list">
                                <%
                                    for (InvoiceItem item : items) {
                                %>
                                <li><%= String.format("%.2f", item.getUnitPrice()) %></li>
                                <%
                                    }
                                %>
                            </ul>
                        </td>

                        <!-- Quantities -->
                        <td>
                            <ul class="product-list">
                                <%
                                    for (InvoiceItem item : items) {
                                %>
                                <li><%= item.getQuantity() %></li>
                                <%
                                    }
                                %>
                            </ul>
                        </td>

                        <!-- GST -->
                        <td>
                            <ul class="product-list">
                                <%
                                    for (InvoiceItem item : items) {
                                %>
                                <li><%= String.format("%.2f", item.getGstAmount()) %></li>
                                <%
                                    }
                                %>
                            </ul>
                        </td>

                        <td class="text-end fw-semibold"><%= String.format("%.2f", inv.getSubtotal()) %></td>
                        <td class="text-end fw-bold text-success"><%= String.format("%.2f", inv.getTotalAmount()) %></td>
                        <td class="text-nowrap text-muted"><%= inv.getCreatedAt() != null ? inv.getCreatedAt() : "" %></td>
                        <td class="text-center">
                            <a href="invoicePDF?id=<%= inv.getId() %>" class="btn btn-sm btn-pdf">
                                📄 PDF
                            </a>
                        </td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="12" class="text-center text-muted">No invoices found.</td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
