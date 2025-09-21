CREATE DATABASE IF NOT EXISTS invoice_app;
USE invoice_app;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) UNIQUE NOT NULL,
  phone VARCHAR(20),
  password VARCHAR(255) NOT NULL
);

select * from users;

CREATE TABLE invoices (
  id INT AUTO_INCREMENT PRIMARY KEY,
  created_by INT NOT NULL,
  customer_name VARCHAR(150) NOT NULL,
  customer_email VARCHAR(150),
  customer_phone VARCHAR(30),
  customer_address VARCHAR(500),
  subtotal DECIMAL(12,2) NOT NULL,
  total_tax DECIMAL(12,2) NOT NULL,
  cgst_amount DECIMAL(12,2) DEFAULT 0,
  sgst_amount DECIMAL(12,2) DEFAULT 0,
  igst_amount DECIMAL(12,2) DEFAULT 0,
  total_amount DECIMAL(12,2) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (created_by) REFERENCES users(id)
);

select * from invoices;

CREATE TABLE invoice_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  invoice_id INT NOT NULL,
  product_name VARCHAR(255) NOT NULL,
  unit_price DECIMAL(12,2) NOT NULL,
  quantity INT NOT NULL,
  gst_rate DECIMAL(5,2) NOT NULL, -- percentage, e.g. 18.00
  taxable_value DECIMAL(12,2) NOT NULL, -- unit_price * quantity
  gst_amount DECIMAL(12,2) NOT NULL,    -- taxable_value * gst_rate/100
  cgst_amount DECIMAL(12,2) DEFAULT 0,
  sgst_amount DECIMAL(12,2) DEFAULT 0,
  igst_amount DECIMAL(12,2) DEFAULT 0,
  FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE
);

select * from invoice_items;
