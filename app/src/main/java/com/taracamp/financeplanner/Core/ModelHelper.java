package com.taracamp.financeplanner.Core;

import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.TransactionCategory;

import java.util.ArrayList;
import java.util.List;

public class ModelHelper {
    public TransactionCategory generateTransactionCategory(String name,String cycle,String transactionType,Double value) {
        TransactionCategory transactionCategory = new TransactionCategory();
        transactionCategory.setTransactionCategoryNme(name);
        transactionCategory.setTransactionCycle(cycle);
        transactionCategory.setTransactionType(transactionType);
        transactionCategory.setTransactionDefaultValue(value);
        return transactionCategory;
    }

    public List<TransactionCategory> generateTransactionCategoryList(){
        List<TransactionCategory> transactionCategories = new ArrayList<>();

        transactionCategories.add(generateTransactionCategory("Gehalt","MONTH","POSITIVE",0.0));
        transactionCategories.add(generateTransactionCategory("Fitness","MONTH","NEGATIVE",0.0));
        transactionCategories.add(generateTransactionCategory("KFZ-Versicherung","MONTH","NEGATIVE",0.0));
        transactionCategories.add(generateTransactionCategory("Miete","MONTH","NEGATIVE",0.0));
        transactionCategories.add(generateTransactionCategory("Handy","MONTH","NEGATIVE",0.0));
        transactionCategories.add(generateTransactionCategory("Amazon Prime","FULLYEAR","NEGATIVE",0.0));
        transactionCategories.add(generateTransactionCategory("Netflix","MONTH","NEGATIVE",0.0));
        transactionCategories.add(generateTransactionCategory("Spotify","MONTH","NEGATIVE",0.0));
        transactionCategories.add(generateTransactionCategory("Sparen","NOTHING","NEGATIVE",0.0));

        return transactionCategories;
    }
}
