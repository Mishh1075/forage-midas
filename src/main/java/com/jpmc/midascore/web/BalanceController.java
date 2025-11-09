package com.jpmc.midascore.web;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping
    public Balance getBalance(@RequestParam("userId") Long userId) {

        // If user does not exist → return balance = 0
        if (!userRepository.existsById(userId)) {
            return new Balance(0f);
        }

        // Sum BigDecimal values for user
        BigDecimal total = StreamSupport.stream(transactionRepository.findAll().spliterator(), false)
                .map(tr -> {
                    BigDecimal amt = tr.getAmount();
                    if (amt == null) amt = BigDecimal.ZERO;

                    if (tr.getRecipientId() != null && tr.getRecipientId().equals(userId)) {
                        return amt; // incoming
                    }

                    if (tr.getSenderId() != null && tr.getSenderId().equals(userId)) {
                        return amt.negate(); // outgoing
                    }

                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Convert BigDecimal → float (Balance class requires float)
        return new Balance(total.floatValue());
    }
}
