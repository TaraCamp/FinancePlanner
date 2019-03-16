package com.taracamp.financeplanner.Models;

public class TransactionCategory {
    private String transactionCategoryNme;
    private String transactionType;
    private String transactionCycle;
    private Double transactionDefaultValue;
    private int transactionDay;
    private int transactionMonth;

    public String getTransactionCategoryNme() {
        return transactionCategoryNme;
    }

    public void setTransactionCategoryNme(String transactionCategoryNme) {
        this.transactionCategoryNme = transactionCategoryNme;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionCycle() {
        return transactionCycle;
    }

    public void setTransactionCycle(String transactionCycle) {
        this.transactionCycle = transactionCycle;
    }

    public Double getTransactionDefaultValue() {
        return transactionDefaultValue;
    }

    public void setTransactionDefaultValue(Double transactionDefaultValue) {
        this.transactionDefaultValue = transactionDefaultValue;
    }

    public int getTransactionDay() {
        return transactionDay;
    }

    public void setTransactionDay(int transactionDay) {
        this.transactionDay = transactionDay;
    }

    public int getTransactionMonth() {
        return transactionMonth;
    }

    public void setTransactionMonth(int transactionMonth) {
        this.transactionMonth = transactionMonth;
    }
}
