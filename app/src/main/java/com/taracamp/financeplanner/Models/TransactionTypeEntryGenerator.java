package com.taracamp.financeplanner.Models;

import java.util.List;

public class TransactionTypeEntryGenerator {

    private static List<TransactionTypeEntry> entries;

    public static List<TransactionTypeEntry> generate(){
        entries.add(getEntry("Gehalt",TransactionTypeEnum.REVENUE,TransactionCycleEnum.MONTH));
        entries.add(getEntry("Fitness",TransactionTypeEnum.EXPENDITURE,TransactionCycleEnum.MONTH));
        entries.add(getEntry("KFZ-Versicherung",TransactionTypeEnum.EXPENDITURE,TransactionCycleEnum.MONTH));
        entries.add(getEntry("Miete",TransactionTypeEnum.EXPENDITURE,TransactionCycleEnum.MONTH));
        entries.add(getEntry("Handy",TransactionTypeEnum.EXPENDITURE,TransactionCycleEnum.MONTH));
        entries.add(getEntry("Amazon Prime",TransactionTypeEnum.EXPENDITURE,TransactionCycleEnum.FULLYEAR));
        entries.add(getEntry("Netflix",TransactionTypeEnum.EXPENDITURE,TransactionCycleEnum.MONTH));
        entries.add(getEntry("Spotify",TransactionTypeEnum.EXPENDITURE,TransactionCycleEnum.MONTH));
        entries.add(getEntry("Sparen",TransactionTypeEnum.TRANSFER,TransactionCycleEnum.NOTHING));

        return entries;
    }

    private static TransactionTypeEntry getEntry(String name,TransactionTypeEnum transactionTypeEnum,
                                                 TransactionCycleEnum transactionCycleEnum){
        TransactionTypeEntry transactionTypeEntry = new TransactionTypeEntry();
        transactionTypeEntry.setName(name);
        transactionTypeEntry.setTransactionType(transactionTypeEnum);
        transactionTypeEntry.setTransactionCycle(transactionCycleEnum);

        return transactionTypeEntry;
    }
}
