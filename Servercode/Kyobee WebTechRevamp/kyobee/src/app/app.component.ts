import { Component, Inject } from '@angular/core';
import { AuthService } from './core/services/auth.service';
import { DOCUMENT } from '@angular/common';
import { DesignService } from './core/services/design.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  public getUserLoggedIn() {
    return this.authService.getLogflag();
  }
  constructor(
    private authService: AuthService,
    private designService: DesignService,
    @Inject(DOCUMENT) private document: Document
  ) {
    this.loadStyle('theme-admin');
    // this.loadStyle('theme-advantech');
    // this.loadStyle('theme-masterkim');
    // this.loadStyle('theme-rbsushi');
    // this.loadStyle('theme-sweethoneydessert');
  }

  /* Change Theme of Stylesheet */
  loadStyle(styleName: string) {
    this.designService.setTheme(styleName);

    const themeLink = this.document.getElementById('client-theme') as HTMLLinkElement;
    styleName += '.css';
    themeLink.href = styleName;
  }
}
