import { Injectable } from '@angular/core';
import { SessionStorageService } from 'angular-web-storage';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  currentDate: Date;
  public loggedIn: any;

  constructor(public session: SessionStorageService) {}

  // loggedIn variable store true that indicate any user is logged in.
  SetLogFlag(): void {
    this.loggedIn = true;
    this.session.set('loggedIn', this.loggedIn);
  }

  // get loggedIn flag stored in session.
  getLogflag(): String {
    return this.session.get('loggedIn');
  }

  // removeLogFlag is deleted from cookies on clicking of logOut button
  removeLoginData(): void {
    this.session.set('loggedIn', '');
    this.session.set('userDTO', null);
  }

  setSessionData(data) {
    this.session.set('userDTO', data);
  }

  // getting a user stored in session.
  getUser(): any {
    return this.session.get('userDTO');
  }
}
