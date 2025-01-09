import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { ApiService } from 'src/app/api.service';
import { NavigationExtras, Router } from '@angular/router';
import { ExceededDialogComponent } from 'src/app/exceeded-dialog/exceeded-dialog.component';
import { Budget } from './budget';

@Component({
  selector: 'app-edit-expense-dialog',
  templateUrl: './edit-expense-dialog.component.html',
  styleUrls: ['../../earning/add-earning-dialog.component.css']
})

export class EditExpenseDialogComponent {
  user: string;
  amount: number;
  date: Date;
  selectedCategory: string;
  expenseId: number;
  errorMessage: string;
  submitted: boolean = false;
  emptyFields: boolean = false;
  categories: string[];
  budgetExceeded: boolean = false; 

  constructor(
    public apiService: ApiService,
    public dialogRef: MatDialogRef<EditExpenseDialogComponent>,
    private router: Router,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialog: MatDialog
  ) {}

  ngOnInit(){
    this.apiService.getCategories().subscribe(
      (data: any)=>{
      this.categories = data;
      },
      (error)=>{
        this.errorMessage = error.error;
        console.log(this.errorMessage);
      }
    );
    this.selectedCategory = this.data.selectedCategory;
    this.data.budgets = this.data.budgets;
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    console.log(this.date);
    this.submitted = true;
    if (this.amount == null || this.date == null || !this.selectedCategory) {
      this.emptyFields = true;
      return;
    }

    const budget = this.data.budgets.find((b: Budget) => b.categoryName === this.selectedCategory);
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
    
    const expense = {
      expensesAmount: this.amount,
      expensesDate: this.date,
    };
    this.apiService.editExpense(this.data.expenseId, this.date, this.amount, this.selectedCategory).subscribe(
      (response: any) => {
        
        const token = localStorage.getItem('currentUser');
    
        if (token !== null) {
          const currentUser = JSON.parse(token);
          this.user = currentUser.token;
        }
        const navigationExtras: NavigationExtras = {
          queryParams: {
            username: this.user,
            rand: Math.random() 
          },
        };
        return this.router.navigate(['/dashboard'], navigationExtras).then(() => {
          this.dialogRef.close(expense);
          window.location.reload();
        });
        
      },
      (error) => {
        this.errorMessage = error.error;
        console.log(this.errorMessage);
        this.dialogRef.close(expense);
      }
    );
  }

  getDefaultDate() {
    return new Date(this.data.expenseDate).toLocaleDateString();
  }
}
