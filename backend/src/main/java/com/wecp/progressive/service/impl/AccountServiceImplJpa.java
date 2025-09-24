package com.wecp.progressive.service.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wecp.progressive.entity.Accounts;
import com.wecp.progressive.repository.AccountRepository;
import com.wecp.progressive.service.AccountService;

@Service
public class AccountServiceImplJpa implements AccountService {

    // @Autowired
    private AccountRepository accountRepository;

    public AccountServiceImplJpa(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Accounts> getAllAccounts() throws SQLException {
        return accountRepository.findAll();
    }

    @Override
    public List<Accounts> getAccountsByUser(int userId) throws SQLException {
        return accountRepository.getAccountsByUser(userId);
    }

    @Override
    public Accounts getAccountById(int accountId) throws SQLException {
        return accountRepository.findById(accountId).get();
    }

    @Override
    public int addAccount(Accounts accounts) throws SQLException {
        return accountRepository.save(accounts).getAccountId();
    }

    @Override
    public void updateAccount(Accounts accounts) throws SQLException {
        // Accounts acc = getAccountById(accounts.getAccountId());
        // acc.setCustomerId(accounts.getCustomerId());
        // acc.setBalance(accounts.getBalance());
        accountRepository.save(accounts);
    }

    @Override
    public void deleteAccount(int accountId) throws SQLException {
        accountRepository.deleteById(accountId);
    }

    @Override
    public List<Accounts> getAllAccountsSortedByBalance() throws SQLException {
        List<Accounts> sortedAccounts = getAllAccounts();
        Collections.sort(sortedAccounts);
        return sortedAccounts;
    }
    
}