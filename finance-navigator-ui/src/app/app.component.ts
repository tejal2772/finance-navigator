import { Component } from '@angular/core';
import { Router, NavigationEnd, RouterEvent } from '@angular/router';
import { filter } from 'rxjs/operators';
import { ApiService } from './api.service';

@Component({
  selector: 'app-root',
  template: `
    <router-outlet></router-outlet>
  `,
  styleUrls: ['./login/login.component.css'],
})
export class AppComponent {
  title = 'finance-navigator-ui';
  isDashboardRoute = false;
  
  constructor(public apiService: ApiService) {
  }

  // constructor(public apiService: ApiService, private router: Router) {
  //   // Listen to route changes to determine if the current route is the dashboard route
  //   this.router.events
  //     .pipe(filter((event: any) => event instanceof NavigationEnd))
  //     .subscribe((event: NavigationEnd) => {
  //       console.log(event.url);
  //       if(event.url === '/login' || event.url === '/signup'){
  //         this.isDashboardRoute = false;
  //       }else if(event.url.startsWith('/dashboard')){
  //         this.isDashboardRoute = true;
  //       }
  //     });
  // }

  logout() {
    this.apiService.logout();
  }
}
