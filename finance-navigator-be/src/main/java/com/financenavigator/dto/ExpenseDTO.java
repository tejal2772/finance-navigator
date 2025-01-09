package com.financenavigator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.financenavigator.entity.Category;
import com.financenavigator.entity.Users;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenseDTO {
    private Long expenseId;

    private double expenseAmount;

    private LocalDate expenseDate;

    private Users users;

    private String categoryName;
}
