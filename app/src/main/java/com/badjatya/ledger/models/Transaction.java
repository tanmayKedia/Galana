package com.badjatya.ledger.models;

import java.util.UUID;

/**
 * Created by Tanmay on 9/27/2016.
 */
public class Transaction {
    String id;
    String girviTransactionId;
    Double amount;
    long date;

    public Transaction() {
        id= UUID.randomUUID().toString();
    }

    public Transaction(String id, String girviTransactionId, Double amount, long date) {
        this.id = id;
        this.girviTransactionId = girviTransactionId;
        this.amount = amount;
        this.date = date;
    }

    public Transaction(String girviTransactionId, Double amount, long date) {
        id= UUID.randomUUID().toString();
        this.girviTransactionId = girviTransactionId;
        this.amount = amount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGirviTransactionId() {
        return girviTransactionId;
    }

    public void setGirviTransactionId(String girviTransactionId) {
        this.girviTransactionId = girviTransactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", girviTransactionId='" + girviTransactionId + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
