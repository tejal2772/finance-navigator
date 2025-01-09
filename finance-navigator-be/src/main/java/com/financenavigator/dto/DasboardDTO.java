package com.financenavigator.dto;

import com.financenavigator.entity.Earnings;
import com.financenavigator.entity.Goals;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DasboardDTO {

    private String user;
    private List<Earnings> earnings;
    private List<ExpenseDTO> expenses;
    private List<BudgetDTO> budgets;
    private List<GoalsDTO> goals;
    private double remainingBalance;

}
