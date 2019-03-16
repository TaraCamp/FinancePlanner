package com.taracamp.financeplanner.Models;

import java.util.Date;

public class Transaction {
    private String transactionName;
    private Double transactionValue;
    private Date transactionDate;
    private Date transactionCreateDate;
    private Account transactionFromAccount;
    private Account transactionToAccount;
    private String transactionType;
    private String transactionDescription;
    private boolean transactionForecast;
    private String transactionCategory;

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Double getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(Double transactionValue) {
        this.transactionValue = transactionValue;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getTransactionCreateDate() {
        return transactionCreateDate;
    }

    public void setTransactionCreateDate(Date transactionCreateDate) {
        this.transactionCreateDate = transactionCreateDate;
    }

    public Account getTransactionFromAccount() {
        return transactionFromAccount;
    }

    public void setTransactionFromAccount(Account transactionFromAccount) {
        this.transactionFromAccount = transactionFromAccount;
    }

    public Account getTransactionToAccount() {
        return transactionToAccount;
    }

    public void setTransactionToAccount(Account transactionToAccount) {
        this.transactionToAccount = transactionToAccount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public boolean isTransactionForecast() {
        return transactionForecast;
    }

    public void setTransactionForecast(boolean transactionForecast) {
        this.transactionForecast = transactionForecast;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }
}
