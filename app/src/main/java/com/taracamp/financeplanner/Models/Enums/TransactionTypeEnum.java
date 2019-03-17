package com.taracamp.financeplanner.Models.Enums;

public enum TransactionTypeEnum {
    POSITIVE("Einnahmen"),
    NEGATIVE("Ausgaben"),
    NEUTRAL("Transfer");

    private String typeValue;

    TransactionTypeEnum(String tValue){
        typeValue = tValue;
    }

    @Override
    public String toString() {
        return typeValue;
    }
}
