package com.taracamp.financeplanner.Models;

public class TransactionTypeEntry {
    private String name;
    private TransactionTypeEnum transactionType;
    private TransactionCycleEnum transactionCycle;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransactionTypeEnum getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeEnum transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionCycleEnum getTransactionCycle() {
        return transactionCycle;
    }

    public void setTransactionCycle(TransactionCycleEnum transactionCycle) {
        this.transactionCycle = transactionCycle;
    }
}
