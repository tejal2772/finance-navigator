// login.component.ts
import { Component, OnInit } from '@angular/core';
import { Router, NavigationExtras } from '@angular/router';
import { ApiService } from '../api.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  username: string;
  password: string;
  errorMessage: string;
  submitted: boolean = false;
  emptyFields: boolean = false;

  private loggedIn = false;

  constructor(
    public apiService: ApiService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {}

  // private configError: MatSnackBarConfig = {
  //   panelClass: ['style-error'],
  // };

  onSubmit() {
    this.submitted = true;
    if(this.username == null || this.password == null){
      this.emptyFields = true;
      return 
    }
    this.apiService.login(this.username, this.password).subscribe(
      
      (data: any) => {
        this.loggedIn = true;
        const navigationExtras: NavigationExtras = {
          queryParams: {
            username: this.username
          }
        };
        localStorage.setItem(
          'currentUser',
          JSON.stringify({ token: this.username})
        );
        this.router.navigate(['/dashboard'], navigationExtras);
      },
      (error) => {
        // this.snackBar.open('Incorrect username or password', 'Dismiss', {duration:5000});
        this.errorMessage = error.error;
        console.log(this.errorMessage);
      }
    );
  }

  get isLoggedIn() {
    return this.loggedIn;
  }
}
