package com.taracamp.financeplanner.Test;

import com.taracamp.financeplanner.Models.Account;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.User;

import java.util.ArrayList;
import java.util.List;

public class Dummy {
    public User generateUser(String email,String token){

        Account account = new Account();
        account.setToken(token);
        account.setName("Tagesgeld");
        account.setValue(5423.32);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);

        Transaction transaction = new Transaction();
        transaction.setToken("43fg43t435r34");
        transaction.setName("Amazon");
        transaction.setValue(4213.23);

        Transaction transaction1 = new Transaction();
        transaction1.setToken("65546");
        transaction1.setName("Bar");
        transaction1.setValue(300.23);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transaction1);

        User user = new User();
        user.setEmail(email);
        user.setUsername("Test");
        user.setToken(token);
        user.setAccounts(accounts);
        user.setTransactions(transactions);

        return user;
    }
}
