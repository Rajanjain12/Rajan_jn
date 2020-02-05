import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';
import { LoginDTO } from 'src/app/core/models/loginDTO.model';
import { JsonPipe } from '@angular/common';
import { LoaderService } from 'src/app/core/services/loader.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  user: LoginDTO = new LoginDTO();

  // flag denoting invalid login i.e wrong username or password.
  invalidLogin: Boolean = false;
  loading = false;
  constructor(private userService: UserService, private authService: AuthService, private router: Router,private loaderService:LoaderService) { }

  ngOnInit() {
  }
  validateLogin(invalid) {

    this.loaderService.show();
    if (invalid) {

      return;
    }
   
    this.user.deviceType="Web";
    this.user.deviceToken="";
    this.user.clientBase="admin";
    console.log("user "+JSON.stringify(this.user));
    this.userService.login(this.user).subscribe((res: any) => {
      var respData = res;
      console.log("log== "+JSON.stringify(respData));
      if (respData.success == 1) {
        this.loading = false;
        this.invalidLogin = false;
        this.authService.SetLogFlag();
        this.authService.setSessionData(respData.serviceResult);
        this.router.navigateByUrl('/waitlist/dashboard', { replaceUrl: true });
      } else {
        this.loading = false;
        alert("username password wrong");
        this.invalidLogin = true;
      }
    });
  }

}
