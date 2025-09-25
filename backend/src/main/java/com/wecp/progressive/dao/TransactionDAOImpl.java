package com.wecp.progressive.dao;

import java.sql.Statement;
import java.sql.Connection;
// import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wecp.progressive.config.DatabaseConnectionManager;
import com.wecp.progressive.entity.Transactions;

public class TransactionDAOImpl implements TransactionDAO {

    private Connection conn;

    public TransactionDAOImpl() {
        try {
            conn = DatabaseConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        };
    }

    @Override
    public int addTransaction(Transactions transaction) throws SQLException {
        String q = "insert into transactions values(?, ?, ?, ?, ?)";

        try(PreparedStatement ps = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, transaction.getTransactionId());
            ps.setInt(2, transaction.getAccount().getAccountId());
            ps.setDouble(3, transaction.getAmount());
            ps.setDate(4, new java.sql.Date((transaction.getTransactionDate()).getTime()));
            ps.setString(5, transaction.getTransactionType());
            if (ps.executeUpdate()>0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next())
                    transaction.setTransactionId(rs.getInt(1));
            }
            return transaction.getTransactionId();
        }
    }

    @Override
    public Transactions getTransactionById(int transactionId) throws SQLException {
        String q = "select * from transactions where transaction_id = ?";

        try(PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToTransactions(rs);
            }
            return null;
        }
    }

    @Override
    public void updateTransaction(Transactions transaction) throws SQLException {
        String q = "update transactions set account_id = ?, amount = ?, transaction_date = ?, transaction_type = ? where transaction_id = ?";

        try(PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, transaction.getAccount().getAccountId());
            ps.setDouble(2, transaction.getAmount());
            ps.setDate(3, new java.sql.Date((transaction.getTransactionDate()).getTime()));
            ps.setString(4, transaction.getTransactionType());
            ps.setInt(5, transaction.getTransactionId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteTransaction(int transactionId) throws SQLException {
        String q = "delete from transactions where transaction_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, transactionId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Transactions> getAllTransactions() throws SQLException {
        String q = "select * from transactions";
        List<Transactions> transactionsList = new ArrayList<>();
        try (Statement s = conn.createStatement()) {
            ResultSet rs = s.executeQuery(q);
            while(rs.next()) {
                transactionsList.add(mapRowToTransactions(rs));
            }
            return transactionsList;
        }
    }
                
    private Transactions mapRowToTransactions(ResultSet rs) throws SQLException {
        Transactions ts = new Transactions();
        ts.setTransactionId(rs.getInt(1));
        ts.setAccountId(rs.getInt(2));
        ts.setAmount(rs.getDouble(3));
        ts.setTransactionDate(new java.util.Date(rs.getDate(4).getTime()));
        ts.setTransactionType(rs.getString(5));
        return ts;
    }
}
