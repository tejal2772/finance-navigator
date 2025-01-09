import { Component, Input } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ApiService } from '../api.service';
import { CreateGoalDialogComponent } from './create-goal-dialog/create-goal-dialog.component';

@Component({
  selector: 'app-goal',
  templateUrl: './goal.component.html',
  styleUrls: ['./goal.component.css']
})
export class GoalComponent {
  @Input() data: any;

  constructor(public dialog: MatDialog, public apiService: ApiService){}

  openCreateGoalDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = this.data;

    const dialogRef = this.dialog.open(CreateGoalDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((result) => {
      
    });



  }


  deleteGoal(goalName: string){
    console.log("Deleting "+goalName);
    this.apiService.deleteGoal(goalName).subscribe(
      ()=>{
        console.log("Refreshing")
        window.location.reload();
      }
    );
    
  }
}
