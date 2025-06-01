package com.jpmc.midascore.kafka;

import com.jpmc.midascore.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TransactionListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionListener.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TransactionService transactionService;
    private final KafkaListenerEndpointRegistry registry;

    // AtomicInteger for thread-safe counting
    private final AtomicInteger messageCount = new AtomicInteger(0);
    private static final int MAX_MESSAGES = 5;

    public TransactionListener(TransactionService transactionService,
                               KafkaListenerEndpointRegistry registry) {
        this.transactionService = transactionService;
        this.registry = registry;
    }

    @KafkaListener(id = "txn-listener", topics = "${midas.kafka.topic}", groupId = "transaction-group")
    public void listen(String message) {
        try {
            Transaction txn = objectMapper.readValue(message, Transaction.class);

            log.info("💸 New Transaction Received:");
            log.info("   👤 Sender ID    : {}", txn.getSenderId());
            log.info("   📥 Recipient ID: {}", txn.getRecipientId());
            log.info("   💰 Amount      : {}", txn.getAmount());

            transactionService.transfer(
                    (long) txn.getSenderId(),
                    (long) txn.getRecipientId(),
                    BigDecimal.valueOf(txn.getAmount())
            );

            // Increment message count
            int count = messageCount.incrementAndGet();
            log.info("📊 Processed messages: {}", count);

            // Check if max messages reached, then stop listener
            if (count >= MAX_MESSAGES) {
                log.info("🛑 Max messages processed ({}). Stopping Kafka listener...", MAX_MESSAGES);
                registry.getListenerContainer("txn-listener").stop();
                log.info("✅ Kafka listener stopped after {} messages.", MAX_MESSAGES);
            }

        } catch (IllegalArgumentException e) {
            log.warn("❌ Transaction rejected: {}", e.getMessage());
        } catch (Exception e) {
            log.error("🚨 Failed to process Kafka message: {}", message, e);
        }
    }

    // Transaction POJO (unchanged)
    public static class Transaction {
        private int senderId;
        private int recipientId;
        private double amount;

        public Transaction() {}

        public int getSenderId() { return senderId; }
        public void setSenderId(int senderId) { this.senderId = senderId; }

        public int getRecipientId() { return recipientId; }
        public void setRecipientId(int recipientId) { this.recipientId = recipientId; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
    }
}
