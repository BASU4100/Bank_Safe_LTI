package com.wecp.progressive.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Accounts implements Comparable<Accounts> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer accountId;
    // private int customerId;
    private double balance;
    @ManyToOne
    @JoinColumn(name="customerId")
    private Customers customer;
    
    public Accounts() {
    }
    
    public Accounts(int accountId, int customerId, double balance) {
        this.accountId = accountId;
        this.customer.setCustomerId(customerId);
        this.balance = balance;
    }
    
    // public Accounts(double balance, Customers customers) {
    //     this.balance = balance;
    //     this.customers = customers;
    // }
    
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    // public int getCustomerId() {
    //     return customerId;
    // }

    public void setCustomerId(int customerId) {
        this.customer.setCustomerId(customerId);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public Customers getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    @Override
    public int compareTo(Accounts otherAccounts) {
        return Double.compare(this.getBalance(), otherAccounts.getBalance());
    }    
}