package com.wecp.progressive.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Accounts implements Comparable<Accounts> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer accountId;
    private int customerId;
    private double balance;
    
    public Accounts() {
    }

    public Accounts(int accountId, int customerId, double balance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public int compareTo(Accounts otherAccounts) {
        return Double.compare(this.getBalance(), otherAccounts.getBalance());
    }    
}