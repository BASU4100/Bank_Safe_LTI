package com.wecp.progressive.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wecp.progressive.entity.Accounts;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Integer> {
    // @Query("select a from Accounts a where a.customerId = :customerId")
    // public List<Accounts> getAccountsByUser(int customerId);
    
    @Query("select a from Accounts a where a.customer.customerId = :customerId")
    public List<Accounts> getAccountsByCustomerCustomerId(int customerId);

    public Accounts findByAccountId(int accountId);
    
    @Modifying
    @Transactional
    @Query("Delete from Accounts acc where acc.customer.customerId = :customerId")
    public void deleteByCustomerId(int customerId);
}
