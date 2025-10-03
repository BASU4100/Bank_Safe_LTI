package com.wecp.progressive.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long loanId;
    private String loanType;
    private double amount;
    private int duration;
    
    public Loan() {
    }

    public Loan(Long loanId, String loanType, double amount, int duration) {
        this.loanId = loanId;
        this.loanType = loanType;
        this.amount = amount;
        this.duration = duration;
    }

    public Long getId() {
        return loanId;
    }

    public void setId(Long loanId) {
        this.loanId = loanId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}