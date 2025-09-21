<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Create Invoice</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script>
        function addRow() {
            const table = document.getElementById("productTable");
            const row = table.insertRow(-1);
            row.innerHTML = `
                <td><input type="text" name="product_name" class="form-control" required></td>
                <td><input type="number" name="unit_price" class="form-control" step="0.01" required oninput="calculateTotals()"></td>
                <td><input type="number" name="quantity" class="form-control" value="1" min="1" required oninput="calculateTotals()"></td>
                <td><input type="number" name="gst_rate" class="form-control" step="0.01" required oninput="calculateTotals()"></td>
                <td><input type="text" name="taxable_value" class="form-control" readonly></td>
                <td><input type="text" name="gst_amount" class="form-control" readonly></td>
                <td><input type="text" name="line_total" class="form-control" readonly></td>
                <td><button type="button" class="btn btn-danger" onclick="removeRow(this)">X</button></td>
            `;
        }

        function removeRow(btn) {
            const row = btn.parentNode.parentNode;
            row.parentNode.removeChild(row);
            calculateTotals();
        }

        function calculateTotals() {
            let subtotal = 0, totalTax = 0, total = 0;
            const rows = document.querySelectorAll("#productTable tr");

            rows.forEach(row => {
                const price = parseFloat(row.querySelector("input[name='unit_price']")?.value) || 0;
                const qty = parseInt(row.querySelector("input[name='quantity']")?.value) || 0;
                const gstRate = parseFloat(row.querySelector("input[name='gst_rate']")?.value) || 0;

                const taxable = price * qty;
                const gst = taxable * gstRate / 100;
                const lineTotal = taxable + gst;

                if (!isNaN(taxable)) row.querySelector("input[name='taxable_value']").value = taxable.toFixed(2);
                if (!isNaN(gst)) row.querySelector("input[name='gst_amount']").value = gst.toFixed(2);
                if (!isNaN(lineTotal)) row.querySelector("input[name='line_total']").value = lineTotal.toFixed(2);

                subtotal += taxable;
                totalTax += gst;
                total += lineTotal;
            });

            document.getElementById("subtotal").value = subtotal.toFixed(2);
            document.getElementById("total_tax").value = totalTax.toFixed(2);
            document.getElementById("total_amount").value = total.toFixed(2);
        }
    </script>
</head>
<body class="container mt-4">
    <h2>Create Invoice</h2>
    <form action="createInvoice" method="post">
        <div class="mb-3">
            <label>Customer Name</label>
            <input type="text" name="customer_name" class="form-control" required>
        </div>
        <div class="mb-3">
            <label>Email</label>
            <input type="email" name="customer_email" class="form-control">
        </div>
        <div class="mb-3">
            <label>Phone</label>
            <input type="text" name="customer_phone" class="form-control">
        </div>
        <div class="mb-3">
            <label>Address</label>
            <textarea name="customer_address" class="form-control"></textarea>
        </div>

        <h4>Products</h4>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Unit Price (₹)</th>
                    <th>Qty</th>
                    <th>GST %</th>
                    <th>Taxable</th>
                    <th>GST Amt</th>
                    <th>Line Total</th>
                    <th></th>
                </tr>
            </thead>
            <tbody id="productTable"></tbody>
        </table>
        <button type="button" class="btn btn-primary" onclick="addRow()">+ Add Product</button>

        <h4 class="mt-3">Totals</h4>
        <div class="row">
            <div class="col-md-4">
                <label>Subtotal</label>
                <input type="text" id="subtotal" name="subtotal" class="form-control" readonly>
            </div>
            <div class="col-md-4">
                <label>Total Tax</label>
                <input type="text" id="total_tax" name="total_tax" class="form-control" readonly>
            </div>
            <div class="col-md-4">
                <label>Total Amount</label>
                <input type="text" id="total_amount" name="total_amount" class="form-control" readonly>
            </div>
        </div>

        <div class="mt-3">
            <button type="submit" class="btn btn-success">Save & Download PDF</button>
        </div>
        
        <div class="mt-3">
		    <!-- Save Invoice -->
		    <button type="submit" name="action" value="save" class="btn btn-success">Save</button>
		
		    <!-- Download PDF -->
		    <button type="submit" name="action" value="download" class="btn btn-primary">Download PDF</button>
		</div>
        
        
    </form>
</body>
</html>
