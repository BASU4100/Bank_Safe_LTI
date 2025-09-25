package com.wecp.progressive.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wecp.progressive.config.DatabaseConnectionManager;
import com.wecp.progressive.entity.Accounts;

public class AccountDAOImpl implements AccountDAO {
    // private List<Accounts> accountsList = new ArrayList<Accounts>();

    private Connection connection;
    
    public AccountDAOImpl() {
        try {
            connection = DatabaseConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Accounts> getAllAccounts() throws SQLException {
        String q = "select * from accounts";
        List<Accounts> accountsList = new ArrayList<>();
        try (Statement s = connection.createStatement()) {
            ResultSet rs = s.executeQuery(q);
            while (rs.next()) {
                accountsList.add(mapRowToAccounts(rs));
            }
        }
        return accountsList;
    }

    public List<Accounts> getAllAccountsByCustomer(int customer_id) throws SQLException {
        String q = "select * from accounts where customer_id = ?";
        List<Accounts> accountsList = new ArrayList<>();
        
        try (PreparedStatement ps = connection.prepareStatement(q)) {
            ps.setInt(1, customer_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                accountsList.add(mapRowToAccounts(rs));
            }
        }
        return accountsList;
    }

    @Override
    public Accounts getAccountById(int accountId) throws SQLException {
        String q = "select * from accounts where account_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(q)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapRowToAccounts(rs);
        }
        return null;
    }

    @Override
    public int addAccount(Accounts accounts) throws SQLException {
        String q = "insert into accounts values(?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, accounts.getAccountId());
            // ps.setInt(2, accounts.getCustomerId());
            ps.setInt(2, accounts.getCustomers().getCustomerId());
            ps.setDouble(3, accounts.getBalance());
            if (ps.executeUpdate()>0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next())
                    accounts.setAccountId(rs.getInt(1));
            }
            return accounts.getAccountId();
        }
    }

    @Override
    public void updateAccount(Accounts accounts) throws SQLException {
        String q = "update accounts set customer_id = ?, balance = ? where account_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            // ps.setInt(1, accounts.getCustomerId());
            ps.setInt(1, accounts.getCustomers().getCustomerId());
            ps.setDouble(2, accounts.getBalance());
            ps.setInt(3, accounts.getAccountId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteAccount(int accountId) throws SQLException {
        String q = "delete from accounts where account_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(q)) {
            ps.setInt(1, accountId);
            ps.executeUpdate();
        }
    }

    private Accounts mapRowToAccounts(ResultSet rs) throws SQLException {
        Accounts acc = new Accounts();
        acc.setAccountId(rs.getInt(1));
        acc.setCustomerId(rs.getInt(2));
        acc.setBalance(rs.getDouble(3));
        return acc;
    }
}
