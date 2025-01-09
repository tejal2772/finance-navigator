// dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../api.service';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  data = {
    remainingBalance: 0,
    totalEarnings: 0,
    totalExpenses: 0
  };;
  username: string;
  errorMessage: string;

  constructor(
    private apiService: ApiService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
    const token = localStorage.getItem('currentUser');
    
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;
      this.username = tokenValue;
    }else{
      this.username = "default";
    }
   
  }

  ngOnInit() {
    const token = localStorage.getItem('currentUser');
    console.log("Dashboard!!")
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;

      this.apiService.getDashboardData(tokenValue).subscribe(
        (data: any) => {
          this.data = data;
        },
        (error) => {
          this.errorMessage = error.error.message;
        }
      );
    } else {
      this.snackBar.open('Log in again to continue', 'Dismiss', {
        duration: 5000,
      });
      console.log('Log in again to continue');
      this.router.navigate(['/login']);
    }
  }


  logout() {
    this.apiService.logout();
  }
}
