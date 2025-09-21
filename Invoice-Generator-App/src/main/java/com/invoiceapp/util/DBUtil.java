package com.invoiceapp.util;
import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/invoice_app";
    private static final String USER = "root"; // your DB user
    private static final String PASS = "Avinash@2002"; // your DB password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
