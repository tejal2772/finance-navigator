package com.financenavigator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.financenavigator.entity.Category;
import com.financenavigator.entity.Users;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class BudgetDTO {

    private Long budgetId;

    private double monthlyBudgetAmount;

    private double remainingBudgetAmount;

    private Users users;

    private String categoryName;

    // Constructors, getters, and setters
}