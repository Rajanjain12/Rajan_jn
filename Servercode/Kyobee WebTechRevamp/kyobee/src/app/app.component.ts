import { Component, Inject } from '@angular/core';
import { AuthService } from './core/services/auth.service';
import { DOCUMENT } from '@angular/common';
import { environment } from './../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'kyobee';

  private loggedInUser: any;

  public getUserLoggedIn() {
    return this.authService.getLogflag();
  }
  constructor(private authService: AuthService, @Inject(DOCUMENT) private document: Document) {
    this.loadStyle('theme-admin');
    // this.loadStyle('theme-advantech');
  }

  /* Change Theme of Stylesheet */
  loadStyle(styleName: string) {
    console.log('______________ Prod', environment.production);
    const themeLink = this.document.getElementById('client-theme') as HTMLLinkElement;
    styleName += '.css';
    themeLink.href = styleName;
  }
}
