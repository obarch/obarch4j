package com.obarch.stat;


import com.obarch.Transaction;

import java.util.function.Consumer;

public class Counter implements Consumer<Transaction> {
    @Override
    public void accept(Transaction tx) {
    }
}
