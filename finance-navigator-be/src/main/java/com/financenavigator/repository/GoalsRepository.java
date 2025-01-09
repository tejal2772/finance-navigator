package com.financenavigator.repository;

import com.financenavigator.entity.Earnings;
import com.financenavigator.entity.Goals;
import com.financenavigator.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalsRepository extends JpaRepository<Goals, Long> {
    List<Goals> findByUsers(Users user);

    List<Goals> findByUsersAndGoalName(Users user, String goalName);

}

