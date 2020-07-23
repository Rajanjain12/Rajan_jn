import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/core/services/user.service';
import { ResetpasswordDTO } from 'src/app/core/models/resetPasswordDTO.model';
import { HttpParams } from '@angular/common/http';

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
  validUrl = true;

  ngOnInit() {
    this.resetpasswordDTO = new ResetpasswordDTO();
    this.resetpasswordDTO.userId = Number(this.route.snapshot.paramMap.get('userId'));
    this.resetpasswordDTO.authcode = String(this.route.snapshot.paramMap.get('authCode'));
    this.validateResetPwdUrl();
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

  validateResetPwdUrl() {
    const params = new HttpParams()
      .set('userId', this.route.snapshot.paramMap.get('userId'))
      .set('authCode', this.route.snapshot.paramMap.get('authCode'));

    this.userService.validateResetPwdUrl(params).subscribe(
      data => {
        console.log('response of validateResetPwdUrl', data.serviceResult);
        if (data.success === 0) {
          this.validUrl = false;
        } else if (data.success === 1) {
        }
      },
      error => {
        console.log('TCL: ResetPasswordComponent -> validateResetPwdUrl -> error', error);
      }
    );
  }
}
