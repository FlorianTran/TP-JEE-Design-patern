package com.JEE.demo.factory;

import com.JEE.demo.entity.*;

public class LoanFactory {

    private LoanFactory() {}

    public static Loan createStandardLoan(User user, Book book) {
        return new Loan(user, book);
    }
}
