package com.financenavigator.repository;

import com.financenavigator.entity.Budget;
import com.financenavigator.entity.Category;
import com.financenavigator.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUsers(Users user);

    List<Budget> findByCategory(Category category);

    List<Budget> findByUsersAndCategory(Users user, Category category);

    Budget findByBudgetId(long parseLong);
}

