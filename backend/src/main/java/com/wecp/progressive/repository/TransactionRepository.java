package com.wecp.progressive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wecp.progressive.entity.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Integer> {

    Transactions findByAccountAccountId(int accountId);

    void deleteByAccountAccountId(int accountId);

    @Modifying
    @Transactional
    @Query("delete from Transactions t where t.account.customer.customerId = :customerId")
    void deleteByCustomerId(int customerId);
}
