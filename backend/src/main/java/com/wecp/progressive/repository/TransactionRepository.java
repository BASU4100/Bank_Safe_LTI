package com.wecp.progressive.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wecp.progressive.entity.Transactions;
// import com.wecp.progressive.exception.AccountNotFoundException;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Integer> {

    List<Transactions> findByAccountsAccountId(Integer accountId);

    void deleteByAccountsAccountId(Integer accountId);

    @Modifying
    @Transactional
    @Query("delete from Transactions t where t.accounts in (Select a from Accounts a where a.customer.customerId = :customerId)")
    void deleteByCustomerId(Integer customerId);
}
