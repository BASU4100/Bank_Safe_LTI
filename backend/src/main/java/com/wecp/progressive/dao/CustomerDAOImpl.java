package com.wecp.progressive.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wecp.progressive.config.DatabaseConnectionManager;
import com.wecp.progressive.dto.CustomerAccountInfo;
import com.wecp.progressive.entity.Customers;

public class CustomerDAOImpl implements CustomerDAO {
    private Connection conn;

    public CustomerDAOImpl() {
        try {
            conn = DatabaseConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        };
    }

    @Override
    public int addCustomer(Customers customers) throws SQLException {
        String q = "insert into customers values(?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customers.getCustomerId());
            ps.setString(2, customers.getName());
            ps.setString(3, customers.getEmail());
            ps.setString(4, customers.getUsername());
            ps.setString(5, customers.getPassword());
            ps.setString(6, customers.getRole());
            if (ps.executeUpdate()>0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next())
                    customers.setCustomerId(rs.getInt(1));
            }
            return customers.getCustomerId();
        }
    }

    @Override
    public Customers getCustomerById(int customerId) throws SQLException {
        String q = "select * from customers where customer_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapRowToCustomers(rs);
        }
        return null;
    }

    @Override
    public void updateCustomer(Customers customers) throws SQLException{
        String q = "update customers set name = ?, email = ?, username = ?, password = ?, role = ? where customer_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, customers.getName());
            ps.setString(2, customers.getEmail());
            ps.setString(3, customers.getUsername());
            ps.setString(4, customers.getPassword());
            ps.setString(5, customers.getRole());
            ps.setInt(6, customers.getCustomerId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteCustomer(int customerId) throws SQLException {
        String q = "delete from customers where customer_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Customers> getAllCustomers() throws SQLException {
        String q = "select * from customers";
        List<Customers> customersList = new ArrayList<>();
        try (Statement s = conn.createStatement()) {
            ResultSet rs = s.executeQuery(q);
            while (rs.next()) {
                customersList.add(mapRowToCustomers(rs));
            }
        }
        return customersList;
    }
             
    @Override
    public CustomerAccountInfo getCustomerAccountInfo(int customerId) throws SQLException {
        String q = "select c.customer_id, name, email, account_id, balance from customers c join accounts a on c.customer_id = a.customer_id where c.customer_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CustomerAccountInfo cai = new CustomerAccountInfo();
                cai.setCustomerId(rs.getInt(1));
                cai.setCustomerName(rs.getString(2));
                cai.setEmail(rs.getString(3));
                cai.setAccountId(rs.getInt(4));
                cai.setBalance(rs.getDouble(5));
                return cai;
            }
            return null;
        }
    }
    
    private Customers mapRowToCustomers(ResultSet rs) throws SQLException {
        Customers cust = new Customers();
        cust.setCustomerId(rs.getInt(1));
        cust.setName(rs.getString(2));
        cust.setEmail(rs.getString(3));
        cust.setUsername(rs.getString(4));
        cust.setPassword(rs.getString(5));
        cust.setRole(rs.getString(6));
        return cust;
    }
}
