package com.financenavigator.service;

import com.financenavigator.dto.*;
import com.financenavigator.entity.*;
import com.financenavigator.repository.*;
import com.financenavigator.util.GoalStatus;
import com.financenavigator.util.TransactionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private GoalsRepository goalsRepository;

    @Autowired
    private EarningsRepository earningsRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users createUser(UserDTO userDTO) {
        Users user = new Users();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
        return user;
    }

    public boolean doesUserExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public Users login(String username, String password) {
        Users user = userRepository.findByUsername(username);
        if(user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        return user;
    }

    public Users findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public ResponseEntity<?> getDashboardDetails(String username) {

        Users user = findUserByUsername(username);
        if(null != user){
            DasboardDTO dasboardDTO = new DasboardDTO();
            dasboardDTO.setUser(user.getUsername());

            LocalDate startOftheMonthDate = LocalDate.now().withDayOfMonth(1);

            // TODO : get Earning details
            List<Earnings> earnings = earningsRepository.findByUsersIdAndEarningDateRange(user.getUserId(), startOftheMonthDate);
            double totalEarning = 0;
            if(earnings != null){
                for(Earnings e: earnings){
                    e.setUsers(null);
                    totalEarning = totalEarning + e.getEarningsAmount();
                }
            }
            List<Earnings> sortedEarnings = earnings.stream()
                    .sorted(Comparator.comparing(Earnings::getEarningsDate).reversed())
                    .collect(Collectors.toList());
            dasboardDTO.setEarnings(sortedEarnings);

            // TODO : get Expense details
            List<Expense> expenses = expenseRepository.findByUsersIdAndExpenseDateRange(user.getUserId(), startOftheMonthDate);
            List<ExpenseDTO> expenseDTOS = new ArrayList<>();
            Map<String, Double> categoryExpenseMap = new HashMap<>();
            double totalExpenses = 0;
            if(expenses!=null){
                expenses.forEach(e->e.setUsers(null));
                for(Expense e: expenses){
                    ExpenseDTO expenseDTO = new ExpenseDTO();
                    expenseDTO.setCategoryName(e.getCategory().getCategoryName());
                    expenseDTO.setExpenseAmount(e.getExpenseAmount());
                    expenseDTO.setExpenseDate(e.getExpenseDate());
                    expenseDTO.setExpenseId(e.getExpenseId());
                    expenseDTOS.add(expenseDTO);
                    totalExpenses = totalExpenses + e.getExpenseAmount();

                    if(categoryExpenseMap.containsKey(e.getCategory().getCategoryName())){
                        categoryExpenseMap.put(e.getCategory().getCategoryName(), categoryExpenseMap.get(e.getCategory().getCategoryName()) + e.getExpenseAmount());
                    }else {
                        categoryExpenseMap.put(e.getCategory().getCategoryName(), e.getExpenseAmount());
                    }
                }
            }
            List<ExpenseDTO> sortedExpenses = expenseDTOS.stream()
                    .sorted(Comparator.comparing(ExpenseDTO::getExpenseDate).reversed())
                    .collect(Collectors.toList());
            dasboardDTO.setExpenses(sortedExpenses);

            // TODO : get Budget details
            List<Budget> budgets = budgetRepository.findByUsers(user);
            List<BudgetDTO> budgetDTOS = new ArrayList<>();
            if(budgets!=null){
                for(Budget e: budgets) {
                    BudgetDTO budgetDTO = new BudgetDTO();
                    budgetDTO.setBudgetId(e.getBudgetId());
                    budgetDTO.setCategoryName(e.getCategory().getCategoryName());
                    budgetDTO.setMonthlyBudgetAmount(e.getMonthlyBudgetAmount());
                    log.info(e.getCategory().getCategoryName());
                    log.info(categoryExpenseMap.containsKey(e.getCategory().getCategoryName())+"");
                    if(!categoryExpenseMap.containsKey(e.getCategory().getCategoryName())){
                        budgetDTO.setRemainingBudgetAmount(e.getMonthlyBudgetAmount());
                    }else{
                        budgetDTO.setRemainingBudgetAmount(e.getMonthlyBudgetAmount()-categoryExpenseMap.get(e.getCategory().getCategoryName()));
                    }
                    budgetDTOS.add(budgetDTO);
                }
            }
            dasboardDTO.setBudgets(budgetDTOS);

            // TODO : get Goals details
            List<Goals> goals = goalsRepository.findByUsers(user);
            List<GoalsDTO> goalsDTOS = new ArrayList<>();
            if(goals!=null){
                for(Goals goal : goals){
                    goal.setUsers(null);
                    GoalsDTO goalsDTO = new GoalsDTO();
                    goalsDTO.setGoalName(goal.getGoalName());
                    goalsDTO.setGoalId(goal.getGoalId());
                    goalsDTO.setGoalAmount(goal.getGoalAmount());
                    goalsDTO.setCreatedDate(goal.getCreatedDate());
                    goalsDTO.setTargetDate(goal.getTargetDate());

                    // TODO :
                    Period period = Period.between(goal.getCreatedDate(), goal.getTargetDate());
                    int months = period.getYears()*12 + period.getMonths();
                    goalsDTO.setDurationMonths(months);

                    Period period1 = Period.between(goal.getCreatedDate(), LocalDate.now());
                    int months1 = period1.getYears()*12 + period1.getMonths();
                    goalsDTO.setCompletedMonths(months1);

                    if(LocalDate.now().isBefore(goal.getTargetDate())) {
                        goalsDTO.setGoalStatus(GoalStatus.ON_TRACK.name());
                    }else {
                        goalsDTO.setGoalStatus(GoalStatus.COMPLETED.name());
                    }

                    goalsDTO.setPerMonthAmount(goal.getGoalAmount()/months);

                    goalsDTOS.add(goalsDTO);

                }
            }
            List<GoalsDTO> sortedGoals = goalsDTOS.stream()
                    .sorted(Comparator.comparing(GoalsDTO::getTargetDate).reversed())
                    .collect(Collectors.toList());
            dasboardDTO.setGoals(sortedGoals);

            dasboardDTO.setRemainingBalance(totalEarning-totalExpenses);
            log.info("Dashboard API Success");
            return new ResponseEntity<>(dasboardDTO, HttpStatus.OK);

        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    public ResponseEntity<?> createCategory(String category) {
        Category existingCategory = categoryRepository.findByCategoryName(category);

        if(existingCategory == null){
            Category newCategory = new Category();
            newCategory.setCategoryName(category);
            categoryRepository.save(newCategory);
        }else {
            log.info("Category [{}] already exist", category);
            return new ResponseEntity<>("Category ["+category+"] already exist", HttpStatus.OK);
        }

        return new ResponseEntity<>("Created New Category ["+category+"]", HttpStatus.OK);
    }

    public ResponseEntity<?> findAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();

        if(allCategories != null){
            List<String> strings = new ArrayList<>();
            allCategories.forEach(c-> strings.add(c.getCategoryName()));
            return new ResponseEntity<>(strings, HttpStatus.OK);
        }else {
            log.info("Category Table is empty");
            return new ResponseEntity<>("Category table is empty", HttpStatus.OK);
        }

    }

    public ResponseEntity<?> deleteCategory(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName);

        if(category != null){
            categoryRepository.delete(category);
            log.info("Category deleted successfully");
            return new ResponseEntity("Category deleted successfully", HttpStatus.OK);
        }else {
            log.info("Category not found");
            return new ResponseEntity("Category not found", HttpStatus.OK);
        }

    }

    public ResponseEntity<?> createBudgetForUser(String username, String categoryName, double ammount) {

        Users user = findUserByUsername(username);
        if(null != user){
            Budget budget = new Budget();
            budget.setMonthlyBudgetAmount(ammount);
            if(null != categoryName && !StringUtils.isEmpty(categoryName)) {
                Category category = categoryRepository.findByCategoryName(categoryName);

                // INFO : check if budget already exists for user & category
                if(category==null){
                    return new ResponseEntity<>("Invalid Category", HttpStatus.BAD_REQUEST);
                }


                List<Budget> existingBudget = budgetRepository.findByUsersAndCategory(user, category);

                if(existingBudget != null && !existingBudget.isEmpty()){
                    log.info("Budget for provided category is already exist for the user. Please select different category or modify existing budget");
                    return new ResponseEntity<>("Budget for provided category is already exist for the user. Please select " +
                            "different category or modify existing budget", HttpStatus.BAD_REQUEST);
                }
                budget.setCategory(category);
                budget.setUsers(user);
                budgetRepository.save(budget);
                return new ResponseEntity<>("{\"Monthly Budget has been set for ["+user.getUsername()+"] and category ["+category.getCategoryName()+"]\":true}", HttpStatus.OK);
            }
            List<Budget> existingBudgets =  budgetRepository.findByUsers(user);
            AtomicBoolean flag = new AtomicBoolean(false);
            if(existingBudgets != null && !existingBudgets.isEmpty()){
                existingBudgets.forEach(b-> {
                    if(b.getCategory() == null){
                        flag.set(true);
                    }
                });
            }
            if(flag.get()){
                return new ResponseEntity<>("Budget for provided user is already exist. Please " +
                        "modify existing budget", HttpStatus.BAD_REQUEST);
            }
            budget.setCategory(null);
            budget.setUsers(user);
            budgetRepository.save(budget);
            return new ResponseEntity<>("Monthly Budget has been set for ["+user.getUsername()+"]", HttpStatus.OK);

        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> editBudgetForUser(String username, String categoryName, double updatedAmmount) {
        Users user = findUserByUsername(username);
        if(null != user){
            Budget budget = new Budget();
            budget.setMonthlyBudgetAmount(updatedAmmount);
            if(null != categoryName && !StringUtils.isEmpty(categoryName)) {
                Category category = categoryRepository.findByCategoryName(categoryName);

                // INFO : check if budget already exists for user & category
                if(category==null){
                    return new ResponseEntity<>("Invalid Category", HttpStatus.BAD_REQUEST);
                }

                List<Budget> existingBudget = budgetRepository.findByUsersAndCategory(user, category);

                if(existingBudget != null && !existingBudget.isEmpty()){
                    Budget affectingBudget = existingBudget.get(0);
                    affectingBudget.setMonthlyBudgetAmount(updatedAmmount);
                    budgetRepository.save(affectingBudget);
                    log.info("Budget for provided category and the user has been successfully modified.");
                    return new ResponseEntity<>("Budget for provided category and the user has been successfully modified.", HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("Budget not found for provided user and category", HttpStatus.BAD_REQUEST);
                }
            }

            List<Budget> existingBudgets =  budgetRepository.findByUsers(user);
            List<Budget> affectingBudget = new ArrayList<>();
            if(existingBudgets != null && !existingBudgets.isEmpty()){
                existingBudgets.forEach(b-> {
                    if(b.getCategory() == null){
                        Budget ab = new Budget();
                        ab.setBudgetId(b.getBudgetId());
                        ab.setMonthlyBudgetAmount(updatedAmmount);
                        ab.setUsers(user);
                        affectingBudget.add(ab);
                    }
                });
            }
            if(!affectingBudget.isEmpty()){
                budgetRepository.save(affectingBudget.get(0));
                return new ResponseEntity<>("Budget for provided user is successfully modified", HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Budget not found for provided user", HttpStatus.BAD_REQUEST);
            }
        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> deleteBudgetForUser(String username, String budgetId) {
        Users user = findUserByUsername(username);
        if(null != user){
            Budget budget = budgetRepository.findByBudgetId(Long.parseLong(budgetId));
            if(null == budget){
                log.info("Budget not found with provided budgetId : "+budgetId);
                return new ResponseEntity<>("Budget not found with provided budgetId", HttpStatus.OK);
            }
            budgetRepository.delete(budget);
            return new ResponseEntity<>("{\"Budget has been deleted for the user\":true}", HttpStatus.OK);
        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> createGoalForUser(String username, String goalName, double goalAmmount, LocalDate targetDate) {
        Users user = findUserByUsername(username);
        if(null != user){
            List<Goals> allGoals = goalsRepository.findByUsersAndGoalName(user, goalName);
            if(allGoals.isEmpty()){
                Goals goal = new Goals();
                goal.setUsers(user);
                goal.setGoalName(goalName);
                goal.setGoalAmount(goalAmmount);
                goal.setCreatedDate(LocalDate.now());
                goal.setTargetDate(targetDate);
                goalsRepository.save(goal);
                return new ResponseEntity<>("{\"Goal is successfully created for user\":true}", HttpStatus.OK);
            }else{
                log.info("Goals for user and provided goal name already exists. Please modify existing goal or create goal with other name");
                return new ResponseEntity<>("Goals for user and provided goal name already exists. Please modify existing goal or create goal with other name", HttpStatus.OK);
            }
        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

//    public ResponseEntity<?> editGoalForUser(String username, String goalName, double goalAmmount, LocalDate targetDate) {
//        Users user = findUserByUsername(username);
//        if(null != user){
//            List<Goals> allGoals = goalsRepository.findByUsersAndGoalName(user, goalName);
//            if(!allGoals.isEmpty()){
//                Goals goal = allGoals.get(0);
//                goal.setUsers(user);
//                goal.setGoalName(goalName);
//                if(goalAmmount != 0) {
//                    goal.setGoalAmount(goalAmmount);
//                }
//                if(targetDate!= null) {
//                    goal.setTargetDate(targetDate);
//                }
//                goalsRepository.save(goal);
//                return new ResponseEntity<>("Goal is successfully modified for user", HttpStatus.OK);
//            }else{
//                log.info("No goals found for provided user and goalName");
//                return new ResponseEntity<>("No goals found for provided user and goalName", HttpStatus.OK);
//            }
//        }else {
//            log.info("User not found");
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//    }

    public ResponseEntity<?> deleteGoalForUser(String username, String goalName) {
        Users user = findUserByUsername(username);
        if(null != user){
            List<Goals> allGoals = goalsRepository.findByUsersAndGoalName(user, goalName);
            if(!allGoals.isEmpty()){
                Goals goal = allGoals.get(0);
                goalsRepository.delete(goal);
                log.info("Goal is successfully deleted");
                return new ResponseEntity<>("{\"Goal is successfully deleted\":true}", HttpStatus.OK);
            }else{
                log.info("No goals found for provided user and goalName");
                return new ResponseEntity<>("No goals found for provided user and goalName"+goalName, HttpStatus.OK);
            }
        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    public ResponseEntity<?> createEarningForUser(String username, double amt, LocalDate earningD) {
        Users user = findUserByUsername(username);
        if(null != user){
            Earnings earnings = new Earnings();
            earnings.setUsers(user);
            earnings.setEarningsDate(earningD);
            earnings.setEarningsAmount(amt);
            earningsRepository.save(earnings);
            return new ResponseEntity<>("{\"Earning is successfully added for user\":true}", HttpStatus.OK);
        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> editEarningForUser(String username, String earningsId, double updatedEarningAmmount, LocalDate updatedEarningDate) {
        Users user = findUserByUsername(username);
        if(null != user){
            Earnings earnings = earningsRepository.findByEarningsId(Long.parseLong(earningsId));
            if(earnings == null){
                return new ResponseEntity<>("Earning not found for user and earningId:"+earningsId, HttpStatus.OK);
            }
            if(updatedEarningDate != null) {
                earnings.setEarningsDate(updatedEarningDate);
            }
            if(updatedEarningAmmount != 0) {
                earnings.setEarningsAmount(updatedEarningAmmount);
            }
            earningsRepository.save(earnings);
            return new ResponseEntity<>("{\"Earning is successfully modified for user\":true}", HttpStatus.OK);
        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> deleteEarningForUser(String username, String earningId) {
        Users user = findUserByUsername(username);
        if(null != user){
            List<Earnings> allEarnings = earningsRepository.findByUsersAndEarningsId(user, Long.parseLong(earningId));
            if(!allEarnings.isEmpty()){
                Earnings earnings = allEarnings.get(0);
                earningsRepository.delete(earnings);
                log.info("Earning is successfully deleted");
                return new ResponseEntity<>("{\"Earning is successfully deleted\":true}", HttpStatus.OK);
            }else{
                log.info("No earnings found for provided user and earningsId");
                return new ResponseEntity<>("No earnings found for provided user and earningsId", HttpStatus.OK);
            }
        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    public ResponseEntity<?> createExpenseForUser(String username, String categoryName, double amt, LocalDate expenseD) {
        Users user = findUserByUsername(username);
        if(null != user){
            Category category = categoryRepository.findByCategoryName(categoryName);
            if(category == null){
                return new ResponseEntity<>("Invalid category", HttpStatus.OK);
            }

            Expense expense = new Expense();
            expense.setUsers(user);
            expense.setCategory(category);
            expense.setExpenseDate(expenseD);
            expense.setExpenseAmount(amt);
            expenseRepository.save(expense);
            return new ResponseEntity<>("{\"Expense has been saved for the user\":true}", HttpStatus.OK);
        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> editExpenseForUser(String username, String expenseId, String categoryName, double amt, LocalDate expenseD) {
        Users user = findUserByUsername(username);
        if(null != user){
            Expense expense = expenseRepository.findByExpenseId(Long.parseLong(expenseId));
            expense.setUsers(user);
            if(null != categoryName && StringUtils.isEmpty(categoryName)){
                Category category = categoryRepository.findByCategoryName(categoryName);
                if(category == null){
                    return new ResponseEntity<>("Invalid category", HttpStatus.OK);
                }
                expense.setCategory(category);
            }
            if(null != expenseD) {
                expense.setExpenseDate(expenseD);
            }
            if(amt != 0) {
                expense.setExpenseAmount(amt);
            }
            expenseRepository.save(expense);
            return new ResponseEntity<>("{\"Expense has been updated for the user\":true}", HttpStatus.OK);
        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> deleteExpenseForUser(String username, String expenseId) {
        Users user = findUserByUsername(username);
        if(null != user){
            Expense expense = expenseRepository.findByExpenseId(Long.parseLong(expenseId));
            if(null == expense){
                log.info("Expense not found with provided expenseId : "+expenseId);
                return new ResponseEntity<>("Expense not found with provided expenseId", HttpStatus.OK);
            }
            expenseRepository.delete(expense);
            return new ResponseEntity<>("{\"Expense has been deleted for the user\":true}", HttpStatus.OK);
        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> generateReport(String username) {
        Users user = findUserByUsername(username);
        if(null != user){

            ReportDTO reportDTO = new ReportDTO();
            List<TransactionsDTO> transactionsDTOSList = new ArrayList<>();

            LocalDate startOftheMonthDate = LocalDate.now().withDayOfMonth(1);

            // Get all expenses
            List<Expense> expenses = expenseRepository.findByUsersIdAndExpenseDateRange(user.getUserId(), startOftheMonthDate);
            Map<String, Double> categoryExpenseMap = new HashMap<>();
            double totalExpenses = 0;
            if(expenses!=null){
                expenses.forEach(e->e.setUsers(null));
                for(Expense e: expenses){
                    totalExpenses = totalExpenses + e.getExpenseAmount();
                    TransactionsDTO transactionsDTO = new TransactionsDTO();
                    transactionsDTO.setTransactionType(TransactionTypeEnum.EXPENSE.name());
                    transactionsDTO.setTransactionDate(e.getExpenseDate());
                    transactionsDTO.setTransactionAmount(e.getExpenseAmount());
                    transactionsDTOSList.add(transactionsDTO);
                    if(categoryExpenseMap.containsKey(e.getCategory().getCategoryName())){
                        categoryExpenseMap.put(e.getCategory().getCategoryName(), categoryExpenseMap.get(e.getCategory().getCategoryName()) + e.getExpenseAmount());
                    }else {
                        categoryExpenseMap.put(e.getCategory().getCategoryName(), e.getExpenseAmount());
                    }
                }
            }



            // Get all incomes
            List<Earnings> earnings = earningsRepository.findByUsersIdAndEarningDateRange(user.getUserId(), startOftheMonthDate);
            double totalIncome = 0;
            if(earnings!=null){
                earnings.forEach(e->e.setUsers(null));
                for(Earnings e: earnings){
                    totalIncome = totalIncome + e.getEarningsAmount();
                    TransactionsDTO transactionsDTO = new TransactionsDTO();
                    transactionsDTO.setTransactionType(TransactionTypeEnum.INCOME.name());
                    transactionsDTO.setTransactionDate(e.getEarningsDate());
                    transactionsDTO.setTransactionAmount(e.getEarningsAmount());
                    transactionsDTOSList.add(transactionsDTO);
                }
            }


            // Get all budget
            List<Budget> budgets = budgetRepository.findByUsers(user);
            List<BudgetDTO> budgetDTOS = new ArrayList<>();
            if(budgets!=null){
                for(Budget e: budgets) {
                    BudgetDTO budgetDTO = new BudgetDTO();
                    budgetDTO.setBudgetId(e.getBudgetId());
                    budgetDTO.setCategoryName(e.getCategory().getCategoryName());
                    budgetDTO.setMonthlyBudgetAmount(e.getMonthlyBudgetAmount());
                    log.info(e.getCategory().getCategoryName());
                    log.info(categoryExpenseMap.containsKey(e.getCategory().getCategoryName())+"");
                    if(!categoryExpenseMap.containsKey(e.getCategory().getCategoryName())){
                        budgetDTO.setRemainingBudgetAmount(e.getMonthlyBudgetAmount());
                    }else{
                        budgetDTO.setRemainingBudgetAmount(e.getMonthlyBudgetAmount()-categoryExpenseMap.get(e.getCategory().getCategoryName()));
                    }
                    budgetDTOS.add(budgetDTO);
                }
            }


            // Get all goals
            List<Goals> goals = goalsRepository.findByUsers(user);
            List<GoalsDTO> goalsDTOS = new ArrayList<>();
            if(goals!=null){
                for(Goals goal : goals){
                    goal.setUsers(null);
                    GoalsDTO goalsDTO = new GoalsDTO();
                    goalsDTO.setGoalName(goal.getGoalName());
                    goalsDTO.setGoalId(goal.getGoalId());
                    goalsDTO.setGoalAmount(goal.getGoalAmount());
                    goalsDTO.setCreatedDate(goal.getCreatedDate());
                    goalsDTO.setTargetDate(goal.getTargetDate());

                    // TODO :
                    Period period = Period.between(goal.getCreatedDate(), goal.getTargetDate());
                    int months = period.getYears()*12 + period.getMonths();
                    goalsDTO.setDurationMonths(months);

                    Period period1 = Period.between(goal.getCreatedDate(), LocalDate.now());
                    int months1 = period1.getYears()*12 + period1.getMonths();
                    goalsDTO.setCompletedMonths(months1);

                    if(LocalDate.now().isBefore(goal.getTargetDate())) {
                        goalsDTO.setGoalStatus(GoalStatus.ON_TRACK.name());
                    }else {
                        goalsDTO.setGoalStatus(GoalStatus.COMPLETED.name());
                    }

                    goalsDTO.setPerMonthAmount(goal.getGoalAmount()/months);

                    goalsDTOS.add(goalsDTO);

                }
            }

            List<TransactionsDTO> sortedTransactionsList = transactionsDTOSList.stream()
                    .sorted(Comparator.comparing(TransactionsDTO::getTransactionDate).reversed())
                    .collect(Collectors.toList());

            reportDTO.setBudget(budgetDTOS);
            reportDTO.setTotalExpense(totalExpenses);
            reportDTO.setTotalIncome(totalIncome);
            reportDTO.setTransactions(sortedTransactionsList);
            reportDTO.setCurrentBalance(totalIncome-totalExpenses);
            reportDTO.setGoals(goalsDTOS);

            return new ResponseEntity<>(reportDTO, HttpStatus.OK);
        }else {
            log.info("User not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}

