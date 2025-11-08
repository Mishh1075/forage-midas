
package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue()
    private Long id;

    private Long senderId;
    private Long recipientId;
    private BigDecimal amount;
    private BigDecimal incentive;
    private Instant createdAt = Instant.now();

    public TransactionRecord() {}

    public TransactionRecord(Long senderId, Long recipientId, BigDecimal amount, BigDecimal incentive) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.incentive = incentive;
    }

    public Long getId() { return id; }
    public Long getSenderId() { return senderId; }
    public Long getRecipientId() { return recipientId; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getIncentive() { return incentive; }
    public Instant getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setSenderId(Long v) { this.senderId = v; }
    public void setRecipientId(Long v) { this.recipientId = v; }
    public void setAmount(BigDecimal v) { this.amount = v; }
    public void setIncentive(BigDecimal v) { this.incentive = v; }
    public void setCreatedAt(Instant t) { this.createdAt = t; }
}
