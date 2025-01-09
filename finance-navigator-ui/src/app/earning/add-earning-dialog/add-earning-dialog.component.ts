import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Router, NavigationExtras } from '@angular/router';
import { ApiService } from 'src/app/api.service';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-add-earning-dialog',
  templateUrl: './add-earning-dialog.component.html',
  styleUrls: ['../add-earning-dialog.component.css'],
})
export class AddEarningDialogComponent {
  user: string;
  amount: number;
  date: Date;
  errorMessage: string;
  submitted: boolean = false;
  emptyFields: boolean = false;

  constructor(
    public apiService: ApiService,
    public dialogRef: MatDialogRef<AddEarningDialogComponent>,
    private router: Router,
    private http: HttpClient
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

    this.apiService.addEarning(this.date, this.amount).subscribe(
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
}
