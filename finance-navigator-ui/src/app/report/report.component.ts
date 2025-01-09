import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import {DataInterface} from './DataInterface';
import Chart from 'chart.js/auto';


@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['../dashboard/dashboard.component.css']
})
export class ReportComponent implements OnInit {
  data: DataInterface;
  username: string;
  errorMessage: string;
  currentBalance: number;
  chart: any;

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
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;

      this.apiService.getReport(tokenValue).subscribe(
        (data: any) => {
          this.data = data;
          console.log("Data loaded")
          this.createChart();
        },
        (error: { error: { message: string; }; }) => {
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

  ngOnInit() {
    
  }

  createChart() {
    const canvas: any = document.getElementById('budget-chart');
    const ctx = canvas.getContext('2d');
    this.data.budget.map((item: { monthlyExpenseAmount: number; }) => console.log("XX"+item.monthlyExpenseAmount))
    this.chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: this.data.budget.map((item: { categoryName: any; }) => item.categoryName),
        datasets: [{
          label: 'Monthly Expense',
          data: this.data.budget.map((item: { monthlyBudgetAmount: number; remainingBudgetAmount:number }) => item.monthlyBudgetAmount-item.remainingBudgetAmount),
          backgroundColor: 'rgba(54, 162, 235, 0.2)',
          borderColor: 'rgba(54, 162, 235, 1)',
          borderWidth: 1
        }]
      },
      options: {
        plugins: {
          legend: {
            display: true,
          },
        },
        scales: {
          x: {
            ticks: {
              display: true,
            },
          },
          y: {
            ticks: {
              display: true,
            },
          }
        }
      }
    });
  }
  

  logout() {
    this.apiService.logout();
  }
}
