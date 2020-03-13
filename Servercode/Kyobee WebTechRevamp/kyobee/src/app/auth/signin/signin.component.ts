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
  errorMsg;

  constructor(private userService: UserService, private authService: AuthService, private router: Router) {}

  ngOnInit() {}
  validateLogin(invalid) {
    if (invalid) {
      this.errorMsg = 'Please enter proper values for mandatory fields';
      return;
    }

    this.user.deviceType = 'Web';
    this.user.deviceToken = '';
    this.user.clientBase = 'admin';
    console.log('user ' + JSON.stringify(this.user));
    this.userService.login(this.user).subscribe((res: any) => {
      var respData = res;
      console.log('log== ' + JSON.stringify(respData));
      if (respData.success == 1) {
        this.errorMsg = null;
        this.loading = false;
        this.invalidLogin = false;
        this.authService.SetLogFlag();
        this.userResponse = respData.serviceResult;
        this.authService.setSessionData(this.userResponse);
        this.router.navigateByUrl('/waitlist/dashboard', { replaceUrl: true });
      } else {
        this.loading = false;
        this.errorMsg = respData.message;
        this.invalidLogin = true;
      }
    });
  }

  toggleEye(event) {
    const targetId = event.target.attributes.toggle.value;

    if (document.querySelector(targetId).getAttribute('type') === 'password') {
      document.querySelector(targetId).setAttribute('type', 'text');
      event.target.className = 'grey flaticon-unlocked eye-icon toggle-password font-weight-semibold';
    } else {
      document.querySelector(targetId).setAttribute('type', 'password');
      event.target.className = 'grey flaticon-padlock eye-icon toggle-password font-weight-semibold';
    }
  }
}
