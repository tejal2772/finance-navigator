import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogConfig,
  MatDialogRef,
} from '@angular/material/dialog';
import { NavigationExtras, Router } from '@angular/router';
import { ApiService } from 'src/app/api.service';
import { ExceededDialogComponent } from 'src/app/exceeded-dialog/exceeded-dialog.component';
import { Budget } from 'src/app/expense/edit-expense-dialog/budget';

@Component({
  selector: 'app-create-budget-dialog',
  templateUrl: './create-budget-dialog.component.html',
  styleUrls: ['./create-budget-dialog.component.css'],
})
export class CreateBudgetDialogComponent {
  user: string;
  categories: string[];
  errorMessage: string;
  submitted: boolean = false;
  emptyFields: boolean = false;
  amount: number;
  selectedCategory: string;

  constructor(
    public apiService: ApiService,
    public dialogRef: MatDialogRef<CreateBudgetDialogComponent>,
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
    if (this.amount == null || !this.selectedCategory) {
      this.emptyFields = true;
      return;
    }

    const budget = this.data.budgets?.find(
      (b: Budget) => b.categoryName === this.selectedCategory
    );
    if (budget) {
      const dialogConfig = new MatDialogConfig();
      dialogConfig.width = '400px';
      dialogConfig.panelClass = 'custom-dialog-container';
      dialogConfig.position = { top: '-30%', left: '50%' };
      dialogConfig.data = {
        message: 'Budget for selected category already exist. Please use different category or delete existing budget of selected category. ',
      };
      this.dialog.open(ExceededDialogComponent, dialogConfig);
      return;
    }

    const budget1 = {
      budgetAmount: this.amount,
    };

    this.apiService.createBudget(this.amount, this.selectedCategory).subscribe(
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
            this.dialogRef.close(budget1);
            window.location.reload();
          });
      },
      (error) => {
        this.errorMessage = error.error;
        console.log(this.errorMessage);
        this.dialogRef.close(budget1);
      }
    );
  }
}
