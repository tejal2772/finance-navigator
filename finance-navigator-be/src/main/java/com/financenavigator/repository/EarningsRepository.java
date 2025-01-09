package com.financenavigator.repository;

import com.financenavigator.entity.Earnings;
import com.financenavigator.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EarningsRepository extends JpaRepository<Earnings, Long> {
    List<Earnings> findByUsers(Users user);

    Earnings findByEarningsId(Long id);

    List<Earnings> findByUsersAndEarningsId(Users user, Long earningId);

    @Query(value = "select * from earnings where user_id = :userId and earnings_date >= :date", nativeQuery = true)
    List<Earnings> findByUsersIdAndEarningDateRange(Long userId, LocalDate date);
}
