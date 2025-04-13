package com.jpmc.midascore.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionListener.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${midas.kafka.topic}", groupId = "transaction-group")
    public void listen(String message) {
        try {
            Transaction txn = objectMapper.readValue(message, Transaction.class);

            log.info("💸 New Transaction Received:");
            log.info("   👤 Sender ID    : {}", txn.getSenderId());
            log.info("   📥 Recipient ID: {}", txn.getRecipientId());
            log.info("   💰 Amount      : ${}", txn.getAmount());

            // 💾 Optional: Forward this to your DB / service layer
            // transactionService.process(txn);

        } catch (Exception e) {
            log.error("🚨 Failed to process Kafka message: {}", message, e);
        }
    }

    // 🧱 Data class
    public static class Transaction {
        private int senderId;
        private int recipientId;
        private double amount;

        // 👇 Must-have constructor
        public Transaction() {
        }

        // ✅ Getters and Setters
        public int getSenderId() {
            return senderId;
        }

        public void setSenderId(int senderId) {
            this.senderId = senderId;
        }

        public int getRecipientId() {
            return recipientId;
        }

        public void setRecipientId(int recipientId) {
            this.recipientId = recipientId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}
