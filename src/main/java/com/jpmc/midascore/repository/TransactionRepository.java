package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    // You can add custom queries here, if needed.
}
