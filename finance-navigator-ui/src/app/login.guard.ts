// login.guard.ts
import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {
  constructor(private router: Router) { }

  canActivate() {
    if (localStorage.getItem('currentUser')) {
      this.router.navigate(['/dashboard']);
      return false;
    }

    return true;
  }
}
