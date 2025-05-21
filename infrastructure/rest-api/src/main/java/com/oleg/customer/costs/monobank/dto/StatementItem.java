package com.oleg.customer.costs.monobank.dto;

import java.time.Instant;

public class StatementItem {
    private String id;
    private Instant time;
    private String description;
    private int mcc;
    private int originalMcc;
    private boolean hold;
    private long amount;
    private long operationAmount;
    private int currencyCode;
    private int commissionRate;
    private long cashbackAmount;
    private long balance;
    private String comment;
    private String receiptId;
    private String invoiceId;
    private String counterEdrpou;
    private String counterIban;
    private String counterName;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Instant getTime() { return time; }
    public void setTime(Instant time) { this.time = time; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getMcc() { return mcc; }
    public void setMcc(int mcc) { this.mcc = mcc; }

    public int getOriginalMcc() { return originalMcc; }
    public void setOriginalMcc(int originalMcc) { this.originalMcc = originalMcc; }

    public boolean isHold() { return hold; }
    public void setHold(boolean hold) { this.hold = hold; }

    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }

    public long getOperationAmount() { return operationAmount; }
    public void setOperationAmount(long operationAmount) { this.operationAmount = operationAmount; }

    public int getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(int currencyCode) { this.currencyCode = currencyCode; }

    public int getCommissionRate() { return commissionRate; }
    public void setCommissionRate(int commissionRate) { this.commissionRate = commissionRate; }

    public long getCashbackAmount() { return cashbackAmount; }
    public void setCashbackAmount(long cashbackAmount) { this.cashbackAmount = cashbackAmount; }

    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getReceiptId() { return receiptId; }
    public void setReceiptId(String receiptId) { this.receiptId = receiptId; }

    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }

    public String getCounterEdrpou() { return counterEdrpou; }
    public void setCounterEdrpou(String counterEdrpou) { this.counterEdrpou = counterEdrpou; }

    public String getCounterIban() { return counterIban; }
    public void setCounterIban(String counterIban) { this.counterIban = counterIban; }

    public String getCounterName() { return counterName; }
    public void setCounterName(String counterName) { this.counterName = counterName; }
}
