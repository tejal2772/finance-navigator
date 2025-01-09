// signup.component.ts
import { Component, OnInit } from '@angular/core';
import { Router, NavigationExtras } from '@angular/router';
import { ApiService } from '../api.service';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  username: string;
  // email: string;
  password: string;
  errorMessage: string;
  submitted: boolean = false;
  emptyFields: boolean = false;

  constructor(private apiService: ApiService, private router: Router, private snackBar: MatSnackBar) { }

  ngOnInit() { }

  onSubmit() {
    this.submitted = true;
    if(this.username == null || this.password == null){
      this.emptyFields = true;
      return 
    }
    this.apiService.signup(this.username, this.password).subscribe(
      (data: any) => {
        console.log('YYYYYYYY');
        const navigationExtras: NavigationExtras = {
          queryParams: {
            username: this.username,
          },
        };
        localStorage.setItem(
          'currentUser',
          JSON.stringify({ token: this.username})
        );
        this.router.navigate(['/dashboard'], navigationExtras);
      },
      (error) => {
        this.errorMessage = error.error;
        console.log(this.errorMessage);
        throw error;
      }
    );
  }
}
