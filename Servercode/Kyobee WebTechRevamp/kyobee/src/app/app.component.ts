import { Component, Inject } from '@angular/core';
import { AuthService } from './core/services/auth.service';
import { DOCUMENT } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  public getUserLoggedIn() {
    return this.authService.getLogflag();
  }
  constructor(private authService: AuthService, @Inject(DOCUMENT) private document: Document) {
    this.loadStyle('theme-admin');
    // this.loadStyle('theme-advantech');
  }

  /* Change Theme of Stylesheet */
  loadStyle(styleName: string) {
    const themeLink = this.document.getElementById('client-theme') as HTMLLinkElement;
    styleName += '.css';
    themeLink.href = styleName;
  }
}
