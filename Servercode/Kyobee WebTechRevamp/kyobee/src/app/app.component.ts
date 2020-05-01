import { Component, Inject } from '@angular/core';
import { AuthService } from './core/services/auth.service';
import { DOCUMENT } from '@angular/common';
import { DesignService } from './core/services/design.service';
import { Router } from '@angular/router';
import { LocalStorage } from '@ngx-pwa/local-storage';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  // loggedInUser used to get localstorage  details
  private loggedInUser: any;
  private userExists: any;

  public getUserLoggedIn() {
    return this.authService.getLogflag();
  }
  constructor(
    private authService: AuthService,
    private designService: DesignService,
    private router: Router,
    private localStorage: LocalStorage,
    @Inject(DOCUMENT) private document: Document
  ) {
    this.loadStyle('theme-admin');
    // this.loadStyle('theme-advantech');
    // this.loadStyle('theme-masterkim');
    // this.loadStyle('theme-rbsushi');
    // this.loadStyle('theme-sweethoneydessert');
  }
  ngOnInit() {
    // Check data in localstorage exists or not in application 
    if (localStorage.getItem('kyobeeUser') !== null) {
      console.log('gone');
      this.loggedInUser = localStorage.getItem('UserDetails'); // getting the stored user from localstorage.
      console.log('a' + localStorage.getItem('UserDetails'));
      this.authService.SetLogFlag(); // setting the session data.
      this.authService.setSessionData(JSON.parse(this.loggedInUser));
      console.log('Cookie exists ! You are already logged in... routing to DashBoard... ');
      this.router.navigateByUrl('/waitlist/dashboard', { replaceUrl: true }); // routing to dashboard.
    }
  }

  /* Change Theme of Stylesheet */
  loadStyle(styleName: string) {
    this.designService.setTheme(styleName);

    const themeLink = this.document.getElementById('client-theme') as HTMLLinkElement;
    styleName += '.css';
    themeLink.href = styleName;
  }
}
