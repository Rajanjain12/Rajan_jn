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
  constructor(
    private authService: AuthService,
    @Inject(DOCUMENT) private document: Document
  ) {
     this.loadStyle('theme-admin');
    //this.loadStyle('theme-advantech');
  }

  /* Change Theme of Stylesheet */
  loadStyle(styleName: string) {
    if (environment.production) {
      // console.log(environment.production);

      const themeLink = this.document.getElementById(
        'client-theme'
      ) as HTMLLinkElement;
      console.log('prod');
      styleName += '.css';
      themeLink.href = styleName;

      // console.log(environment.production);

      // const link0 = document.createElement('link');
      // link0.href = 'styles.css';
      // link0.rel = 'stylesheet';
      // document.head.appendChild(link0);

      // const link1 = document.createElement('link');
      // link1.href = styleName + '.css';
      // link1.rel = 'stylesheet';
      // document.head.appendChild(link1);

      // const link2 = document.createElement('link');
      // link2.href = 'combine.css';
      // link2.rel = 'stylesheet';
      // document.head.appendChild(link2);
    } else {
      const script0 = document.createElement('script');
      script0.src = 'styles.js';
      document.head.appendChild(script0);

      const script1 = document.createElement('script');
      script1.src = styleName + '.js';
      document.head.appendChild(script1);

      const script2 = document.createElement('script');
      script2.src = 'combine.js';
      document.head.appendChild(script2);
    }
  }
}
