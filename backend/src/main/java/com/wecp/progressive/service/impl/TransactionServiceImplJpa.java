package com.wecp.progressive.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wecp.progressive.entity.Accounts;
import com.wecp.progressive.entity.Transactions;
import com.wecp.progressive.exception.AccountNotFoundException;
import com.wecp.progressive.exception.OutOfBalanceException;
import com.wecp.progressive.exception.WithdrawalLimitException;
import com.wecp.progressive.repository.AccountRepository;
import com.wecp.progressive.repository.TransactionRepository;
import com.wecp.progressive.service.TransactionService;

@Service
public class TransactionServiceImplJpa implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    
    public TransactionServiceImplJpa(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }
    
    @Override
    public List<Transactions> getAllTransactions() throws SQLException {
        return transactionRepository.findAll();
    }

    @Override
    public Transactions getTransactionById(int transactionId) throws SQLException {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    @Override
    public int addTransaction(Transactions transaction) throws SQLException {
        // update Accounts Logic
        Accounts account = transaction.getAccounts();
        if (transaction.getAmount()>30000) {
            throw new WithdrawalLimitException("Withdrawal limit is 30000");
        }
        double balance = accountRepository.findByAccountId(transaction.getAccounts().getAccountId()).getBalance();
        if (transaction.getTransactionType().equalsIgnoreCase("DEPOSIT")) {
            balance += transaction.getAmount();
        }
        else {
            if(balance < transaction.getAmount()) {
                throw new OutOfBalanceException("Not enough balance");
            }
            else {
                balance -= transaction.getAmount();
            }
        }
        transaction.getAccounts().setBalance(balance);
        account.setBalance(balance);
        accountRepository.save(account);
        return transactionRepository.save(transaction).getTransactionId();
    }

    @Override
    public void updateTransaction(Transactions transaction) throws SQLException {
        transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(int transactionId) throws SQLException {
        transactionRepository.deleteById(transactionId);
    }

    @Override
    public List<Transactions> getTransactionsByCustomerId(int customerId) throws SQLException {
        List<Accounts> accList = accountRepository.getAccountsByCustomerCustomerId(customerId);
        if (accList.isEmpty())
            throw new AccountNotFoundException("Account not found");
        List<Transactions> transactionList = accList.stream().map(acc -> transactionRepository.findByAccountsAccountId(acc.getAccountId())).collect(Collectors.toList());
        return transactionList;
    }
}