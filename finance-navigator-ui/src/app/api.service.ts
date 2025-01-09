import { Injectable } from '@angular/core';
import { Router, NavigationExtras } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class ApiService {

 
  // private apiUrl = 'http://springboot-service.default.svc.cluster.local:8080/api/auth';

  // private apiUrl2 = 'http://springboot-service.default.svc.cluster.local:8080/api';



  private apiUrl = 'http://localhost:8080/api/auth';

  private apiUrl2 = 'http://localhost:8080/api';

  username: string;
  errorMessage: string;

  constructor(
    private http: HttpClient,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  signup(username: string, password: string) {
    return this.http.post(`${this.apiUrl}/signup`, { username, password });
  }

  login(username: string, password: string) {
    return this.http.post(`${this.apiUrl}/login`, { username, password });
  }

  logout() {
    // Call your logout API here and set loggedIn to false if successful
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }

  getReport(username: string): any {
    const params = new HttpParams().set('username', username);
    return this.http.get(`${this.apiUrl2}/report`, { params });
  }

  getDashboardData(username: string) {
    const params = new HttpParams().set('username', username);
    return this.http.get(`${this.apiUrl2}/dashboard`, { params });
  }

  get isLoggedIn() {
    if(null == localStorage.getItem('currentUser')){
      return false;
    }else{
      return true;
    }
  }

  addEarning(earningDate: Date, earningsAmount: number){
    const dateString: string = earningDate.toString();
    const token = localStorage.getItem('currentUser');
    
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;
      const params = new HttpParams().set('username', tokenValue)
      .set('earningAmount', earningsAmount)
      .set('earningDate', dateString);
      return this.http.get(`${this.apiUrl2}/addEarning`, {params});
    }else{
      this.snackBar.open('Log in again to continue', 'Dismiss', {
        duration: 5000,
      });
      console.log('Log in again to continue');
      this.router.navigate(['/login']);
      return this.http.get(`${this.apiUrl2}/addEarning`);
    }

  }

  editEarning(earningId: number, date: Date, amount: number) {
    const dateString: string = date.toString();
    const token = localStorage.getItem('currentUser');
    
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;
      const params = new HttpParams().set('username', tokenValue)
      .set('earningId', earningId)
      .set('updatedEarningAmount', amount)
      .set('updatedEarningDate', dateString);
      return this.http.get(`${this.apiUrl2}/editEarning`, {params});
    }else{
      this.snackBar.open('Log in again to continue', 'Dismiss', {
        duration: 5000,
      });
      console.log('Log in again to continue');
      this.router.navigate(['/login']);
      return this.http.get(`${this.apiUrl2}/editEarning`);
    }
  }

  

  deleteEarning(earningId: number){
    const token = localStorage.getItem('currentUser');
    
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;
      const params = new HttpParams().set('username', tokenValue)
      .set('earningId', earningId);
      return this.http.get(`${this.apiUrl2}/deleteEarning`, {params});
    }else{
      this.snackBar.open('Log in again to continue', 'Dismiss', {
        duration: 5000,
      });
      console.log('Log in again to continue');
      this.router.navigate(['/login']);
      return this.http.get(`${this.apiUrl2}/addEarning`);
    }
  }

  getCategories(){
    return this.http.get(`${this.apiUrl2}/findAllCategories`);
  }

  addExpense(date: Date, amount: number, selectedCategory: string) {
    const dateString: string = date.toString();
    const token = localStorage.getItem('currentUser');
    
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;
      const params = new HttpParams().set('username', tokenValue)
      .set('expenseAmount', amount)
      .set('categoryName', selectedCategory)
      .set('expenseDate', dateString);
      return this.http.get(`${this.apiUrl2}/addExpense`, {params});
    }else{
      this.snackBar.open('Log in again to continue', 'Dismiss', {
        duration: 5000,
      });
      console.log('Log in again to continue');
      this.router.navigate(['/login']);
      return this.http.get(`${this.apiUrl2}/addExpense`);
    }
  }

  editExpense(exepenseId: any, date: Date, amount: number, selectedCategory: string) {
    const dateString: string = date.toString();
    const token = localStorage.getItem('currentUser');
    
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;
      const params = new HttpParams().set('username', tokenValue)
      .set('expenseId', exepenseId)
      .set('updatedExpenseAmount', amount)
      .set('updatedExpenseDate', dateString)
      .set('updatedCategoryName', selectedCategory);
      console.log(exepenseId+amount+date+selectedCategory);
      return this.http.get(`${this.apiUrl2}/editExpense`, {params});
    }else{
      this.snackBar.open('Log in again to continue', 'Dismiss', {
        duration: 5000,
      });
      console.log('Log in again to continue');
      this.router.navigate(['/login']);
      return this.http.get(`${this.apiUrl2}/editExpense`);
    }
  }

  deleteExpense(expenseId: number) {
    const token = localStorage.getItem('currentUser');
    
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;
      const params = new HttpParams().set('username', tokenValue)
      .set('expenseId', expenseId);
      return this.http.get(`${this.apiUrl2}/deleteExpense`, {params});
    }else{
      this.snackBar.open('Log in again to continue', 'Dismiss', {
        duration: 5000,
      });
      console.log('Log in again to continue');
      this.router.navigate(['/login']);
      return this.http.get(`${this.apiUrl2}/addEarning`);
    }
  }

  deleteBudget(budgetId: number) {
    const token = localStorage.getItem('currentUser');
    
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;
      const params = new HttpParams().set('username', tokenValue)
      .set('budgetId', budgetId);
      return this.http.get(`${this.apiUrl2}/deleteBudget`, {params});
    }else{
      this.snackBar.open('Log in again to continue', 'Dismiss', {
        duration: 5000,
      });
      console.log('Log in again to continue');
      this.router.navigate(['/login']);
      return this.http.get(`${this.apiUrl2}/addEarning`);
    }
  }

  createBudget(amount: number, selectedCategory: string) {
    const token = localStorage.getItem('currentUser');
    
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;
      const params = new HttpParams().set('username', tokenValue)
      .set('categoryName', selectedCategory)
      .set('amount', amount);
      return this.http.get(`${this.apiUrl2}/createBudget`, {params});
    }else{
      this.snackBar.open('Log in again to continue', 'Dismiss', {
        duration: 5000,
      });
      console.log('Log in again to continue');
      this.router.navigate(['/login']);
      return this.http.get(`${this.apiUrl2}/addEarning`);
    }
  }

  createGoal(date: Date, amount: number, name: string) {
    const token = localStorage.getItem('currentUser');
    const dateString: string = date.toString();
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;
      const params = new HttpParams().set('username', tokenValue)
      .set('goalName', name)
      .set('goalAmount', amount)
      .set('targetDate', dateString);
      return this.http.get(`${this.apiUrl2}/createGoal`, {params});
    }else{
      this.snackBar.open('Log in again to continue', 'Dismiss', {
        duration: 5000,
      });
      console.log('Log in again to continue');
      this.router.navigate(['/login']);
      return this.http.get(`${this.apiUrl2}/addEarning`);
    }
  }

  deleteGoal(goalName: string) {
    const token = localStorage.getItem('currentUser');
    
    if (token !== null) {
      const currentUser = JSON.parse(token);
      const tokenValue = currentUser.token;
      const params = new HttpParams().set('username', tokenValue)
      .set('goalName', goalName);
      return this.http.get(`${this.apiUrl2}/deleteGoal`, {params});
    }else{
      this.snackBar.open('Log in again to continue', 'Dismiss', {
        duration: 5000,
      });
      console.log('Log in again to continue');
      this.router.navigate(['/login']);
      return this.http.get(`${this.apiUrl2}/addEarning`);
    }
  }



}
