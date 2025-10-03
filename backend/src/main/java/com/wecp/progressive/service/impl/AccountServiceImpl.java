package com.wecp.progressive.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// import com.wecp.progressive.dao.AccountDAO;
import com.wecp.progressive.dao.AccountDAOImpl;
import com.wecp.progressive.entity.Accounts;
import com.wecp.progressive.service.AccountService;

public class AccountServiceImpl implements AccountService  {
    private AccountDAOImpl accountDAO;
    private static List<Accounts> accountsList = new ArrayList<>();

    public AccountServiceImpl(AccountDAOImpl accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public List<Accounts> getAllAccounts() throws SQLException {
        accountsList = accountDAO.getAllAccounts();
        return accountsList;
    }
    
    @Override
    public int addAccount(Accounts accounts) throws SQLException {
        accountsList.add(accounts);
        return accountDAO.addAccount(accounts);
    }
    
    @Override
    public List<Accounts> getAllAccountsSortedByBalance() throws SQLException {
        List<Accounts> sortedAccounts = getAllAccounts();
        Collections.sort(sortedAccounts);
        return sortedAccounts;
    }

    @Override
    public List<Accounts> getAccountsByUser(int userId) throws SQLException {
        return accountDAO.getAllAccountsByCustomer(userId);
    } 

    @Override
    public Accounts getAccountById(int accountId) throws SQLException {
        return accountDAO.getAccountById(accountId);
    }

    @Override
    public void updateAccount(Accounts accounts) throws SQLException {
        accountDAO.updateAccount(accounts);
    }

    @Override
    public void deleteAccount(int accountId) throws SQLException {
        accountDAO.deleteAccount(accountId);
    }
}