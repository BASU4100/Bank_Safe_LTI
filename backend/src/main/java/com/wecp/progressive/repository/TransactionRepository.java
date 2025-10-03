package com.wecp.progressive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wecp.progressive.entity.Transactions;
// import com.wecp.progressive.exception.AccountNotFoundException;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Integer> {

    Transactions findByAccountsAccountId(int accountId);

    void deleteByAccountsAccountId(int accountId);

    @Modifying
    @Transactional
    @Query("delete from Transactions t where t.accounts.customer.customerId = :customerId")
    void deleteByCustomerId(int customerId);
}
