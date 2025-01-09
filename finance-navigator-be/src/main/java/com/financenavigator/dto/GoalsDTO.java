package com.financenavigator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.financenavigator.entity.Users;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class GoalsDTO {
    private Long goalId;
    private String goalName;
    private String goalStatus;
    private double goalAmount;
    private LocalDate createdDate;
    private LocalDate targetDate;
    private Users users;
    private int durationMonths;
    private int completedMonths;
    private double perMonthAmount;

}
