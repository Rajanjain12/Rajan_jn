import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/core/services/user.service';
import { ResetpasswordDTO } from 'src/app/core/models/resetPasswordDTO.model';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
  constructor(private route: ActivatedRoute, private userService: UserService, private router: Router) {}

  newPassword: string; // new password string.
  confirmPwd: string; // confirm password string.
  resetpasswordDTO: ResetpasswordDTO;
  ngOnInit() {
    this.resetpasswordDTO = new ResetpasswordDTO();
    this.resetpasswordDTO.userId = Number(this.route.snapshot.paramMap.get('userId'));
    this.resetpasswordDTO.authcode = String(this.route.snapshot.paramMap.get('authCode'));
  }

  changePwd(invalid) {
    if (invalid) {
      return;
    }
    if (this.confirmPwd != this.newPassword) {
      return;
    }

    this.resetpasswordDTO.password = this.newPassword;

    // API call
    this.userService.resetPwd(this.resetpasswordDTO).subscribe(
      success => {
        console.log('TCL: ResetPasswordComponent -> changePwd -> success', success);
        this.router.navigate(['auth/thankyou'], { state: { pageUrl: '/auth/reset-password' } });
      },
      error => {
        console.log('TCL: ResetPasswordComponent -> changePwd -> error', error);
      }
    );
  }
}
