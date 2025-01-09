package com.financenavigator.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Goals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long goalId;

    @Column(name = "goal_name")
    private String goalName;

    @Column(name = "goal_amount")
    private double goalAmount;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    // Constructors, getters, and setters
}

