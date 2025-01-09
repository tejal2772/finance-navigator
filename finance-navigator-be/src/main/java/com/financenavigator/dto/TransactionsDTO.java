package com.financenavigator.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionsDTO {

    private String transactionType;

    private LocalDate transactionDate;

    private double transactionAmount;

}
