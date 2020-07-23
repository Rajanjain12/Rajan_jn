import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { UserService } from 'src/app/core/services/user.service';
import { User } from 'src/app/core/models/user.model';
import { LoginDTO } from 'src/app/core/models/loginDTO.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  user: User = new User();
  loginUser: LoginDTO = new LoginDTO();
  userResponse: User;

  constructor(private authService: AuthService, private userService: UserService, private router: Router) {}

  ngOnInit() {
    console.log('user in dashboard:' + JSON.stringify(this.authService.getUser()));
    this.user = this.authService.getUser();
  }

  restaurantLogin(restaurant) {
    this.loginUser.deviceType = 'Web';
    this.loginUser.deviceToken = '';
    this.loginUser.clientBase = restaurant.credDTO.clientBase;
    this.loginUser.userName = restaurant.credDTO.userName;
    this.loginUser.password = restaurant.credDTO.password;

    this.userService.login(this.loginUser).subscribe((res: any) => {
      const respData = res;
      console.log('log== ' + JSON.stringify(respData));
      if (respData.success === 1) {
        this.authService.SetLogFlag();
        this.userResponse = respData.serviceResult;
        this.authService.setSessionData(this.userResponse);

        this.router.navigateByUrl('/waitlist/dashboard', { replaceUrl: true });
      } else {
        alert('Error while restaurant login.');
      }
    });
  }
}
