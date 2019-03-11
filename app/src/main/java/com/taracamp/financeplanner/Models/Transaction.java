package com.taracamp.financeplanner.Models;

import java.util.Date;

public class Transaction {
    private String token;
    private String name;
    private Account accountFrom;
    private Account accountTo;
    private Double value;
    private boolean transactionForecast;
    private Date transactionDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public boolean isTransactionForecast() {
        return transactionForecast;
    }

    public void setTransactionForecast(boolean transactionForecast) {
        this.transactionForecast = transactionForecast;
    }
}
