package com.financenavigator.repository;

import com.financenavigator.entity.Category;
import com.financenavigator.entity.Expense;
import com.financenavigator.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUsers(Users user);

    List<Expense> findByCategory(Category category);

    Expense findByExpenseId(long parseLong);

    @Query(value = "select * from expense where user_id = :userId and expense_date >= :date", nativeQuery = true)
    List<Expense> findByUsersIdAndExpenseDateRange(Long userId, LocalDate date);
}

