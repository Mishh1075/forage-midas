package com.jpmc.midascore.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionListener.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final TransactionService transactionService;

    public TransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${midas.kafka.topic}", groupId = "midas-core")
    public void onMessage(String message) {
        try {
            Transaction tx = mapper.readValue(message, Transaction.class);
            transactionService.process(tx);
            log.info("Processed transaction: {} -> {} amount {}", tx.getSenderId(), tx.getRecipientId(), tx.getAmount());
        } catch (Exception e) {
            log.error("Failed to process transaction payload: {}", message, e);
        }
    }
}
