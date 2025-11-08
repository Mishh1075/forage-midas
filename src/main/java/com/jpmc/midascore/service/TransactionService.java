
package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final IncentiveClient incentiveClient;

    public TransactionService(UserRepository userRepository,
                              TransactionRepository transactionRepository,
                              IncentiveClient incentiveClient) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.incentiveClient = incentiveClient;
    }

    @Transactional
    public void process(Transaction tx) {
        if (tx == null) return;
        if (tx.getAmount() <= 0) return;

        Optional<UserRecord> senderOpt = userRepository.findById(tx.getSenderId());
        Optional<UserRecord> recipOpt = userRepository.findById(tx.getRecipientId());
        if (senderOpt.isEmpty() || recipOpt.isEmpty()) return;

        UserRecord sender = senderOpt.get();
        UserRecord recip = recipOpt.get();

        BigDecimal amount = BigDecimal.valueOf(tx.getAmount());
        if (sender.getBalance().compareTo(amount) < 0) {
            return; // insufficient funds; reject
        }

        // Call Incentive API
        Incentive inc = incentiveClient.fetchIncentive(tx);
        BigDecimal incentive = BigDecimal.valueOf(inc.getAmount());
        if (incentive.compareTo(BigDecimal.ZERO) < 0) {
            incentive = BigDecimal.ZERO; // guard
        }

        // Update balances: sender -= amount; recipient += amount + incentive
        sender.setBalance(sender.getBalance().subtract(amount));
        recip.setBalance(recip.getBalance().add(amount).add(incentive));

        // Persist both users and transaction record
        userRepository.save(sender);
        userRepository.save(recip);

        TransactionRecord record = new TransactionRecord(tx.getSenderId(), tx.getRecipientId(), amount, incentive);
        transactionRepository.save(record);
    }
}
