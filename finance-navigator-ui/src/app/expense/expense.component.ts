import { Component, Input } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ApiService } from '../api.service';
import { AddExpenseDialogComponent } from './add-expense-dialog/add-expense-dialog.component';
import { EditExpenseDialogComponent } from './edit-expense-dialog/edit-expense-dialog.component';

@Component({
  selector: 'app-expense',
  templateUrl: './expense.component.html',
  styleUrls: ['../earning/earning.component.css']
})
export class ExpenseComponent {
  @Input() data: any;

  constructor(public dialog: MatDialog, public apiService: ApiService,
){}

openAddExpenseDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = this.data;

    const dialogRef = this.dialog.open(AddExpenseDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((result) => {
      
    });
  }

  openEditExpenseDialog(expenseId: number, expenseAmount:any, expenseDate:any, category:any) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = this.data;
    dialogConfig.data.expenseId = expenseId;
    dialogConfig.data.expenseAmount = expenseAmount;
    dialogConfig.data.expenseDate = expenseDate;
    dialogConfig.data.selectedCategory = category;

    const dialogRef = this.dialog.  open(EditExpenseDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((result) => {

    });
  }

  deleteExpense(expenseId: number){
    console.log("Deleting "+expenseId);
    this.apiService.deleteExpense(expenseId).subscribe(
      ()=>{
        console.log("Refreshing")
        window.location.reload();
      }
    );
    
  }
}
