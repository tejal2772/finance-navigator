package com.financenavigator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportDTO {

    // Category wise expenses & budget
    private List<BudgetDTO> budget;

    private double currentBalance;

    private double totalIncome;

    private double totalExpense;

    private List<GoalsDTO> goals;

    private List<TransactionsDTO> transactions;



}
