package com.taracamp.financeplanner.Models;

import java.util.Date;

public class Account {
    private Double accountValue;
    private String accountName;
    private String accountDescription;
    private Date accoutCreateDate;
    private String accountType;
    private boolean accountRecordToValue;

    public Double getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(Double accountValue) {
        this.accountValue = accountValue;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

    public Date getAccoutCreateDate() {
        return accoutCreateDate;
    }

    public void setAccoutCreateDate(Date accoutCreateDate) {
        this.accoutCreateDate = accoutCreateDate;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isAccountRecordToValue() {
        return accountRecordToValue;
    }

    public void setAccountRecordToValue(boolean accountRecordToValue) {
        this.accountRecordToValue = accountRecordToValue;
    }
}
