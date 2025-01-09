import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogConfig,
  MatDialogRef,
} from '@angular/material/dialog';
import { NavigationExtras, Router } from '@angular/router';
import { ApiService } from 'src/app/api.service';
import { Budget } from '../edit-expense-dialog/budget';
import { ExceededDialogComponent } from 'src/app/exceeded-dialog/exceeded-dialog.component';

@Component({
  selector: 'app-add-expense-dialog',
  templateUrl: './add-expense-dialog.component.html',
  styleUrls: ['../../earning/add-earning-dialog.component.css'],
})
export class AddExpenseDialogComponent {
  user: string;
  amount: number;
  selectedCategory: string;
  date: Date;
  errorMessage: string;
  submitted: boolean = false;
  emptyFields: boolean = false;
  categories: string[];
  budgetExceeded: boolean = false;

  constructor(
    public apiService: ApiService,
    public dialogRef: MatDialogRef<AddExpenseDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.apiService.getCategories().subscribe(
      (data: any) => {
        this.categories = data;
      },
      (error) => {
        this.errorMessage = error.error;
        console.log(this.errorMessage);
      }
    );
    this.data.budgets = this.data.budgets;
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.amount == null || this.date == null || !this.selectedCategory) {
      this.emptyFields = true;
      return;
    }

    const budget = this.data.budgets?.find(
      (b: Budget) => b.categoryName === this.selectedCategory
    );
    if (budget && this.amount > budget.remainingBudgetAmount) {
      const dialogConfig = new MatDialogConfig();
      dialogConfig.width = '400px';
      dialogConfig.panelClass = 'custom-dialog-container';
      // dialogConfig.position = { top: '-30%', left: '50%' };
      dialogConfig.data = {
        message: 'The expense amount is exceeding the alloted budget of selected category. Please enter less amount.',
      };
      this.dialog.open(ExceededDialogComponent, dialogConfig);
      return;
    }

    const earning = {
      expenseAmount: this.amount,
      expenseDate: this.date,
    };

    this.apiService
      .addExpense(this.date, this.amount, this.selectedCategory)
      .subscribe(
        (response: any) => {
          const token = localStorage.getItem('currentUser');

          if (token !== null) {
            const currentUser = JSON.parse(token);
            this.user = currentUser.token;
          }
          const navigationExtras: NavigationExtras = {
            queryParams: {
              username: this.user,
              rand: Math.random(),
            },
          };
          return this.router
            .navigate(['/dashboard'], navigationExtras)
            .then(() => {
              this.dialogRef.close(earning);
              window.location.reload();
            });
        },
        (error) => {
          this.errorMessage = error.error;
          console.log(this.errorMessage);
          this.dialogRef.close(earning);
        }
      );
  }
}
