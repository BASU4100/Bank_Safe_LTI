package com.wecp.progressive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wecp.progressive.entity.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Integer> {

    Transactions findByAccountAccountId(int accountId);
}
