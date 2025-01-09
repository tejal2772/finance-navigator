import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'; 


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SignupComponent } from './signup/signup.component';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { EarningComponent } from './earning/earning.component';
import { AddEarningDialogComponent } from './earning/add-earning-dialog/add-earning-dialog.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { EditEarningDialogComponent } from './earning/edit-earning-dialog/edit-earning-dialog.component';
import { ExpenseComponent } from './expense/expense.component';
import { BudgetComponent } from './budget/budget.component';
import { GoalComponent } from './goal/goal.component';
import { AddExpenseDialogComponent } from './expense/add-expense-dialog/add-expense-dialog.component';
import { EditExpenseDialogComponent } from './expense/edit-expense-dialog/edit-expense-dialog.component';
import { ExceededDialogComponent } from './exceeded-dialog/exceeded-dialog.component';
import { CreateBudgetDialogComponent } from './budget/create-budget-dialog/create-budget-dialog.component';
import { ReportComponent } from './report/report.component';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { CreateGoalDialogComponent } from './goal/create-goal-dialog/create-goal-dialog.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    LoginComponent,
    DashboardComponent,
    EarningComponent,
    AddEarningDialogComponent,
    EditEarningDialogComponent,
    ExpenseComponent,
    BudgetComponent,
    GoalComponent,
    AddExpenseDialogComponent,
    EditExpenseDialogComponent,
    ExceededDialogComponent,
    CreateBudgetDialogComponent,
    ReportComponent,
    CreateGoalDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    MatSnackBarModule,
    BrowserAnimationsModule,
    NoopAnimationsModule,
    MatGridListModule,
    MatDialogModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatCardModule,
    MatToolbarModule,
    MatProgressBarModule,
    MatIconModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
