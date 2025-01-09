import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { NavigationExtras, Router } from '@angular/router';
import { ApiService } from 'src/app/api.service';
import { ExceededDialogComponent } from 'src/app/exceeded-dialog/exceeded-dialog.component';

@Component({
  selector: 'app-create-goal-dialog',
  templateUrl: './create-goal-dialog.component.html',
  styleUrls: ['../../earning/add-earning-dialog.component.css'],
})
export class CreateGoalDialogComponent {
  user: string;
  amount: number;
  name: string;
  date: Date;
  errorMessage: string;
  submitted: boolean = false;
  emptyFields: boolean = false;
  categories: string[];
  budgetExceeded: boolean = false;

  constructor(
    public apiService: ApiService,
    public dialogRef: MatDialogRef<CreateGoalDialogComponent>,
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
    this.data.goals = this.data.goals;
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.amount == null || this.date == null || !this.name) {
      this.emptyFields = true;
      return;
    }

    const goals = this.data.goals.find(
      (b: any) => b.goalName === this.name
    );
    if (goals) {
      const dialogConfig = new MatDialogConfig();
      dialogConfig.width = '400px';
      dialogConfig.panelClass = 'custom-dialog-container';
      // dialogConfig.position = { top: '-30%', left: '50%' };
      dialogConfig.data = {
        message: 'Goal with same name alreday exist. Please use different goal name',
      };
      this.dialog.open(ExceededDialogComponent, dialogConfig);
      return;
    }

    const goal = {
      expenseAmount: this.amount,
      expenseDate: this.date,
    };

    this.apiService
      .createGoal(this.date, this.amount, this.name)
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
              this.dialogRef.close(goal);
              window.location.reload();
            });
        },
        (error) => {
          this.errorMessage = error.error;
          console.log(this.errorMessage);
          this.dialogRef.close(goal);
        }
      );
  }
}
