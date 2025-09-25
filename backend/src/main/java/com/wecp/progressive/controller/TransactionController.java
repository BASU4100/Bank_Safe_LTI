package com.wecp.progressive.controller;

import com.wecp.progressive.entity.Transactions;
import com.wecp.progressive.service.impl.AccountServiceImplJpa;
import com.wecp.progressive.service.impl.TransactionServiceImplJpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private final TransactionServiceImplJpa transactionServiceImplJpa;
    @Autowired
    private final AccountServiceImplJpa accountServiceImplJpa;

    public TransactionController(TransactionServiceImplJpa transactionServiceImplJpa, AccountServiceImplJpa accountServiceImplJpa) {
        this.transactionServiceImplJpa = transactionServiceImplJpa;
        this.accountServiceImplJpa = accountServiceImplJpa;
    }

    @GetMapping
    public ResponseEntity<List<Transactions>> getAllTransactions() throws SQLException {
        return new ResponseEntity<>(transactionServiceImplJpa.getAllTransactions(), HttpStatus.OK);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transactions> getTransactionById(@PathVariable int transactionId) throws SQLException {
        return new ResponseEntity<>(transactionServiceImplJpa.getTransactionById(transactionId), HttpStatus.OK);
    }

    // @PostMapping
    // public ResponseEntity<Integer> addTransaction(@RequestBody Transactions transaction) throws SQLException {
    //     return new ResponseEntity<>(transactionServiceImplJpa.addTransaction(transaction), HttpStatus.CREATED);
    // }
    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody Transactions transaction) throws SQLException {
        if (transaction.getAccount().getAccountId()==null) 
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(transactionServiceImplJpa.addTransaction(transaction), HttpStatus.CREATED);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> updateTransaction(@PathVariable int transactionId, @RequestBody Transactions transaction) throws SQLException {
        transaction.setTransactionId(transactionId);
        transactionServiceImplJpa.updateTransaction(transaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int transactionId) throws SQLException {
        transactionServiceImplJpa.deleteTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/{customerId}")
    public ResponseEntity<List<Transactions>> getAllTransactionsByCustomerId(@PathVariable int customerId) throws SQLException {
        return new ResponseEntity<>(transactionServiceImplJpa.getTransactionsByCustomerId(customerId), HttpStatus.OK);
    }
}
