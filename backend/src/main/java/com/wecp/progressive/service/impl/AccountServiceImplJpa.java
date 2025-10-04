package com.wecp.progressive.service.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wecp.progressive.entity.Accounts;
import com.wecp.progressive.exception.AccountNotFoundException;
import com.wecp.progressive.repository.AccountRepository;
import com.wecp.progressive.repository.TransactionRepository;
import com.wecp.progressive.service.AccountService;

@Service
public class AccountServiceImplJpa implements AccountService {

    @Autowired
    private TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public AccountServiceImplJpa(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Accounts> getAllAccounts() throws SQLException {
        return accountRepository.findAll();
    }

    @Override
    public List<Accounts> getAccountsByUser(int customerId) throws SQLException {
        return accountRepository.getAccountsByCustomerCustomerId(customerId);
    }

    @Override
    public Accounts getAccountById(int accountId) {
        Accounts account = accountRepository.findByAccountId(accountId);
        if (account==null) {
            throw new AccountNotFoundException("Account not found with accountId = "+accountId);
        }
        return account;
    }

    @Override
    public int addAccount(Accounts accounts) throws SQLException {
        return accountRepository.save(accounts).getAccountId();
    }

    @Override
    public void updateAccount(Accounts accounts) throws SQLException {
        accountRepository.save(accounts);
    }

    @Override
    public void deleteAccount(int accountId) throws SQLException {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("No account exists with id: "+accountId);
        }
        transactionRepository.deleteByAccountsAccountId(accountId);
        accountRepository.deleteById(accountId);
    }

    @Override
    public List<Accounts> getAllAccountsSortedByBalance() throws SQLException {
        List<Accounts> sortedAccounts = getAllAccounts();
        Collections.sort(sortedAccounts);
        return sortedAccounts;
    }
    
}