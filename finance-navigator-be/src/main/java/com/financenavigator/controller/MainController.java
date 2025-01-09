package com.financenavigator.controller;

import com.financenavigator.entity.Budget;
import com.financenavigator.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Slf4j
public class MainController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/health")
    private String health(){
        return "Green";
    }

    @GetMapping("/dashboard")
    private ResponseEntity<?> getDashboardDetails(@RequestParam String username){
//        log.info("Dashboard API called for user "+username);
        return userService.getDashboardDetails(username);
    }

    @GetMapping("/createcategory")
    private ResponseEntity<?> createCategory(@RequestParam String category){
        return userService.createCategory(category);
    }

    @GetMapping("/deletecategory")
    private ResponseEntity<?> deleteCategory(@RequestParam String category){
        return userService.deleteCategory(category);
    }

    @GetMapping("/findAllCategories")
    private ResponseEntity<?> findAllCategories(){
        return userService.findAllCategories();
    }

    @GetMapping("/createBudget")
    private ResponseEntity<?> createBudgetForUser(@RequestParam String username, @RequestParam(required = false) String categoryName, @RequestParam double amount){
        return userService.createBudgetForUser(username, categoryName, amount);
    }

    @GetMapping("/editBudget")
    private ResponseEntity<?> editBudget(@RequestParam String username, @RequestParam(required = false) String categoryName, @RequestParam double updatedAmount){
        return userService.editBudgetForUser(username, categoryName, updatedAmount);
    }

    @GetMapping("/deleteBudget")
    private ResponseEntity<?> deleteBudget(@RequestParam String username, @RequestParam(required = false) String budgetId){
        return userService.deleteBudgetForUser(username, budgetId);
    }

    @GetMapping("/createGoal")
    private ResponseEntity<?> createGoalForUser(@RequestParam String username, @RequestParam String goalName, @RequestParam double goalAmount, @RequestParam String targetDate){
        try {
            LocalDate targetD = null;
            if (targetDate != null && !StringUtils.isEmpty(targetDate)) {
                targetD = LocalDate.parse(targetDate);
            }else {
                return ResponseEntity.badRequest().body("targetDate cannot be null");
            }
            if(targetD.isBefore(LocalDate.now())){
                return ResponseEntity.badRequest().body("targetDate cannot be in past");
            }
            return userService.createGoalForUser(username, goalName, goalAmount, targetD);
        }catch (DateTimeParseException e){
            return ResponseEntity.badRequest().body("Invalid Date Format. Please use ISO date format (YYYY-MM-DD)");
        }catch (Exception e){
//            log.info("Exception in createGoal"+e.getMessage());
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Exception occurred in createGoal", e);
        }
    }

//    @GetMapping("/editGoal")
//    private ResponseEntity<?> editGoal(@RequestParam String username, @RequestParam String goalName, @RequestParam String updatedGoalAmount, @RequestParam String updatedTargetDate){
//        try {
//            LocalDate targetD = null;
//            if (updatedTargetDate != null && !StringUtils.isEmpty(updatedTargetDate)) {
//                targetD = LocalDate.parse(updatedTargetDate);
//            }
//            double amt = 0;
//            if (updatedGoalAmount != null && !StringUtils.isEmpty(updatedGoalAmount)) {
//                amt = Double.parseDouble(updatedGoalAmount);
//            }
//            return userService.editGoalForUser(username, goalName, amt, targetD);
//        }catch (DateTimeParseException e){
//            return ResponseEntity.badRequest().body("Invalid Date Format. Please use ISO date format (YYYY-MM-DD)");
//        }catch (Exception e){
//            log.info("Exception in editGoal"+e.getMessage());
//            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Exception occurred in editGoal", e);
//        }
//    }

    @GetMapping("/deleteGoal")
    private ResponseEntity<?> deleteGoal(@RequestParam String username, @RequestParam String goalName){
        return userService.deleteGoalForUser(username, goalName);
    }


    @GetMapping("/addEarning")
    private ResponseEntity<?> createEarningForUser(@RequestParam String username, @RequestParam String earningAmount, @RequestParam String earningDate) {
        try {
//            log.info("Add Earning API called for user "+username);
            LocalDate earningD = null;
            if (earningDate != null && !StringUtils.isEmpty(earningDate)) {
                earningD = LocalDate.parse(earningDate);
            }
            double amt = 0;
            if (earningAmount != null && !StringUtils.isEmpty(earningAmount)) {
                amt = Double.parseDouble(earningAmount);
            }
            return userService.createEarningForUser(username, amt, earningD);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid Date Format. Please use ISO date format (YYYY-MM-DD)");
        } catch (NumberFormatException e){
            return ResponseEntity.badRequest().body("Invalid Number Format");
        }
        catch (Exception e) {
//            log.info("Exception in createEarningForUser" + e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception occurred in createEarningForUser", e);
        }
    }

    @GetMapping("/editEarning")
    private ResponseEntity<?> editEarning(@RequestParam String username, @RequestParam String earningId, @RequestParam String updatedEarningAmount, @RequestParam String updatedEarningDate) {
        try {
            LocalDate earningD = null;
            if (updatedEarningDate != null && !StringUtils.isEmpty(updatedEarningDate)) {
                earningD = LocalDate.parse(updatedEarningDate);
            }
            double amt = 0;
            if (updatedEarningAmount != null && !StringUtils.isEmpty(updatedEarningAmount)) {
                amt = Double.parseDouble(updatedEarningAmount);
            }
            return userService.editEarningForUser(username, earningId, amt, earningD);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid Date Format. Please use ISO date format (YYYY-MM-DD)");
        } catch (Exception e) {
//            log.info("Exception in editEarning" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception occurred in editEarning", e);
        }
    }

    @GetMapping("/deleteEarning")
    private ResponseEntity<?> deleteEarning(@RequestParam String username, @RequestParam String earningId){
        return userService.deleteEarningForUser(username, earningId);
    }

    @GetMapping("/addExpense")
    private ResponseEntity<?> createExpenseForUser(@RequestParam String username, @RequestParam String categoryName, @RequestParam String expenseAmount, @RequestParam String expenseDate) {
        try {
            if(categoryName == null || StringUtils.isEmpty(categoryName)){
                return ResponseEntity.badRequest().body("CategoryName cannot be null");
            }
            LocalDate expenseD = null;
            if (expenseDate != null && !StringUtils.isEmpty(expenseDate)) {
                expenseD = LocalDate.parse(expenseDate);
            }else {
                return ResponseEntity.badRequest().body("expenseDate cannot be null");
            }
            double amt = 0;
            if (expenseAmount != null && !StringUtils.isEmpty(expenseAmount)) {
                amt = Double.parseDouble(expenseAmount);
            }else {
                return ResponseEntity.badRequest().body("expenseAmount cannot be null");
            }
            return userService.createExpenseForUser(username, categoryName, amt, expenseD);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid Date Format. Please use ISO date format (YYYY-MM-DD)");
        } catch (Exception e) {
//            log.info("Exception in createExpenseForUser" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception occurred in createExpenseForUser", e);
        }
    }

    @GetMapping("/editExpense")
    private ResponseEntity<?> editExpense(@RequestParam String username, @RequestParam String expenseId, @RequestParam String updatedCategoryName, @RequestParam String updatedExpenseAmount, @RequestParam String updatedExpenseDate) {
        try {
            LocalDate expenseD = null;
            if (updatedExpenseDate != null && !StringUtils.isEmpty(updatedExpenseDate)) {
                expenseD = LocalDate.parse(updatedExpenseDate);
            }
            double amt = 0;
            if (updatedExpenseAmount != null && !StringUtils.isEmpty(updatedExpenseAmount)) {
                amt = Double.parseDouble(updatedExpenseAmount);
            }
            return userService.editExpenseForUser(username, expenseId, updatedCategoryName, amt, expenseD);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid Date Format. Please use ISO date format (YYYY-MM-DD)");
        } catch (Exception e) {
//            log.info("Exception in editExpense" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception occurred in editExpense", e);
        }
    }

    @GetMapping("/deleteExpense")
    private ResponseEntity<?> deleteExpense(@RequestParam String username, @RequestParam String expenseId){
        return userService.deleteExpenseForUser(username, expenseId);
    }

    @GetMapping("/report")
    private ResponseEntity<?> generateReport(@RequestParam String username){
        return userService.generateReport(username);
    }


}
