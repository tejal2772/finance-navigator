import { HttpClient } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { MatDialogRef , MAT_DIALOG_DATA} from '@angular/material/dialog';
import { NavigationExtras, Router } from '@angular/router';
import { ApiService } from 'src/app/api.service';
import { AddEarningDialogComponent } from '../add-earning-dialog/add-earning-dialog.component';

@Component({
  selector: 'app-edit-earning-dialog',
  templateUrl: './edit-earning-dialog.component.html',
  styleUrls: ['../add-earning-dialog.component.css']
})
export class EditEarningDialogComponent {
  user: string;
  amount: number;
  date: Date;
  earningId: number;
  errorMessage: string;
  submitted: boolean = false;
  emptyFields: boolean = false;

  constructor(
    public apiService: ApiService,
    public dialogRef: MatDialogRef<AddEarningDialogComponent>,
    private router: Router,
    private http: HttpClient,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    console.log(this.date);
    this.submitted = true;
    if (this.amount == null || this.date == null) {
      this.emptyFields = true;
      return;
    }
    const earning = {
      earningsAmount: this.amount,
      earningsDate: this.date,
    };

    this.apiService.editEarning(this.data.earningId, this.date, this.amount).subscribe(
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

  getDefaultDate() {
    return new Date(this.data.earningDate).toLocaleDateString();
  }
}
