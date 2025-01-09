package com.financenavigator.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "earnings")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Earnings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "earnings_id")
    private Long earningsId;

    @Column(name = "earnings_amount")
    private double earningsAmount;

    @Column(name = "earnings_date")
    private LocalDate earningsDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    // Constructors, getters, and setters
}

