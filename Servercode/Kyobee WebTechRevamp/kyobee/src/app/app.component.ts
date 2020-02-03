import { Component } from '@angular/core';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'kyobee';
  private loggedInUser: any;

  public getUserLoggedIn(): String { return this.authService.getLogflag() }
  constructor(private authService: AuthService){}
  
}
