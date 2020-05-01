import { Injectable } from '@angular/core';
import { SessionStorageService } from 'angular-web-storage';
import { LocalStorage } from '@ngx-pwa/local-storage';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  currentDate: Date;
  public loggedIn: any;

  constructor(public session: SessionStorageService, private localStorage: LocalStorage) {}

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
  removeLogFlag(): void {
    this.session.set('loggedIn', '');
  }

  setSessionData(data) {
    this.session.set('userDTO', data);
  }

  // getting a user stored in session.
  getUser(): any {
    return this.session.get('userDTO');
  }

  // sets data in local storage
  setLocalStorageData(data): void {
    console.log('user:' + JSON.stringify(data));
    localStorage.setItem('kyobeeUser', 'true');
    localStorage.setItem('UserDetails', JSON.stringify(data));
    console.log('user:' + localStorage.getItem('kyobeeUser'));
    console.log('details:' + localStorage.getItem('UserDetails'));
  }

  // clear user data after logout
  removeLocalStorageData(): void {
    localStorage.clear();
    this.session.clear();
  }
}
