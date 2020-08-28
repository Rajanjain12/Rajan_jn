import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';
import { LoginDTO } from 'src/app/core/models/loginDTO.model';
import { LoaderService } from 'src/app/core/services/loader.service';
import { User } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {
  user: LoginDTO = new LoginDTO();

  // flag denoting invalid login i.e wrong username or password.
  invalidLogin = false;
  loading = false;
  userResponse: User;
  alertError: { type: string; msg: string; display: boolean } = { type: '', msg: '', display: false };

  constructor(private userService: UserService, private authService: AuthService, private router: Router) {}

  ngOnInit() {}
  validateLogin(invalid) {
    if (invalid) {
      this.alertError = { type: 'danger', msg: 'Please enter proper values for mandatory fields', display: true };
      return;
    }

    this.user.deviceType = 'Web';
    this.user.deviceToken = '';
    this.user.clientBase = 'admin';
    this.user.mainOrganizationLogin = false;

    this.userService.login(this.user).subscribe((res: any) => {
      var respData = res;
      console.log('log== ' + JSON.stringify(respData));
      if (respData.success == 1) {
        this.alertError.display = false;
        this.loading = false;
        this.invalidLogin = false;
        this.authService.SetLogFlag();
        this.userResponse = respData.serviceResult;
        this.authService.setSessionData(this.userResponse);

        // When remMe is true then set data in local storage
        if (this.user.rem === true) {
          console.log('Remember me pressed ');
          this.authService.setLocalStorageData(this.userResponse);
        }
        if (respData.serviceResult.orgUserDetailList === null) {
          this.router.navigateByUrl('/waitlist/dashboard', { replaceUrl: true });
        } else {
          this.router.navigateByUrl('/dashboard', { replaceUrl: true });
        }
      } else {
        this.loading = false;
        this.alertError = { type: 'danger', msg: respData.message, display: true };
        this.invalidLogin = true;
      }
    });
  }

  toggleEye(event) {
    const targetId = event.target.attributes.toggle.value;

    if (document.querySelector(targetId).getAttribute('type') === 'password') {
      document.querySelector(targetId).setAttribute('type', 'text');
      event.target.className = 'grey fi flaticon-unlocked font-14 eye-icon toggle-password font-weight-semibold';
    } else {
      document.querySelector(targetId).setAttribute('type', 'password');
      event.target.className = 'grey fi flaticon-padlock font-14 eye-icon toggle-password font-weight-semibold';
    }
  }
}
