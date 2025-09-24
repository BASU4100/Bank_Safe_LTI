package com.wecp.progressive.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wecp.progressive.entity.Accounts;
import com.wecp.progressive.entity.Transactions;
import com.wecp.progressive.repository.AccountRepository;
import com.wecp.progressive.repository.TransactionRepository;
import com.wecp.progressive.service.TransactionService;

@Service
public class TransactionServiceImplJpa implements TransactionService {
    
    // @Autowired
    private TransactionRepository transactionRepository;
    
    // @Autowired
    private AccountRepository accountRepository;
    
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
        return transactionRepository.findById(transactionId).get();
    }

    @Override
    public int addTransaction(Transactions transaction) throws SQLException {
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
        List<Accounts> accList = accountRepository.getAccountsByUser(customerId);
        List<Transactions> transactionList = accList.stream().map(acc -> transactionRepository.findByAccountId(acc.getAccountId())).collect(Collectors.toList());
        return transactionList;
    }

}
