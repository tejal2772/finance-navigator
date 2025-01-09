import { Component, Input } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ApiService } from '../api.service';
import { CreateBudgetDialogComponent } from './create-budget-dialog/create-budget-dialog.component';
// import { CreateBudgetDialogComponent } from './create-budget-dialog/create-budget-dialog.component';

@Component({
  selector: 'app-budget',
  templateUrl: './budget.component.html',
  styleUrls: ['./budget.component.css']
})
export class BudgetComponent {
  @Input() data: any;

  constructor(public dialog: MatDialog, public apiService: ApiService,
    ){}

  createBudget(){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = this.data;

    const dialogRef = this.dialog.open(CreateBudgetDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((result) => {
      
    });

  }


  deleteBudget(budgetId: number){
    console.log("Deleting "+budgetId);
    this.apiService.deleteBudget(budgetId).subscribe(
      ()=>{
        console.log("Refreshing")
        window.location.reload();
      }
    );
    
  }
}
