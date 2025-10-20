package com.wecp.progressive.controller;

import com.wecp.progressive.entity.Accounts;
import com.wecp.progressive.repository.CustomerRepository;
import com.wecp.progressive.repository.TransactionRepository;
import com.wecp.progressive.service.impl.AccountServiceImplJpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountServiceImplJpa accountServiceImplJpa;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public AccountController(AccountServiceImplJpa accountServiceImplJpa) {
        this.accountServiceImplJpa = accountServiceImplJpa;
    }

    @GetMapping
    public ResponseEntity<List<Accounts>> getAllAccounts() throws SQLException {
        return new ResponseEntity<>(accountServiceImplJpa.getAllAccounts(), HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable Integer accountId) throws SQLException {
        return new ResponseEntity<>(accountServiceImplJpa.getAccountById(accountId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Accounts>> getAccountsByUser(@PathVariable Integer userId) throws SQLException {
        return new ResponseEntity<>(accountServiceImplJpa.getAccountsByUser(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Integer> addAccount(@RequestBody Accounts accounts) throws SQLException {
        if (accounts.getCustomer()==null || accounts.getCustomer().getCustomerId()==null || customerRepository.findByCustomerId(accounts.getCustomer().getCustomerId())==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountServiceImplJpa.addAccount(accounts), HttpStatus.CREATED);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Void> updateAccount(@PathVariable Integer accountId, @RequestBody Accounts accounts) throws SQLException {
        if (accounts.getCustomer()==null || accounts.getCustomer().getCustomerId()==null || customerRepository.findByCustomerId(accounts.getCustomer().getCustomerId())==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        accounts.setAccountId(accountId);
        accountServiceImplJpa.updateAccount(accounts);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable int accountId) throws SQLException {
        accountServiceImplJpa.deleteAccount(accountId);
        transactionRepository.deleteByAccountsAccountId(accountId);
    }
}