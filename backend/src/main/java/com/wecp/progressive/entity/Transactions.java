package com.wecp.progressive.entity;
import java.util.Date;
// import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer transactionId;
    // private int accountId;
    private double amount;
    private String transactionType;
    private Date transactionDate;
    @ManyToOne
    @JoinColumn(name = "accountId")
    private Accounts account;
    
    public Transactions() {
    }

    public Transactions(int transactionId, int accountId, double amount, Date transactionDate, String transactionType) {
        this.transactionId = transactionId;
        this.account.setAccountId(accountId);
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public Transactions(double amount, String transactionType, Date transactionDate, Accounts accounts) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.account = accounts;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    // public int getAccountId() {
    //     return accountId;
    // }

    public void setAccountId(int accountId) {
        this.account.setAccountId(accountId);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    } 

    public Accounts getAccount() {
        return account;
    }

    public void setAccount(Accounts accounts) {
        this.account = accounts;
    }
}