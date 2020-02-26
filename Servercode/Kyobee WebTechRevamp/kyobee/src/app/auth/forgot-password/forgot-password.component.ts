import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {
  constructor(private userService: UserService, private router: Router) {}
  username = '';
  resend = false;
  error: { show: boolean; type: string; msg: string } = { show: false, type: '', msg: '' };
  ngOnInit() {}

  sendLinkForPwordReset(invalid) {
    if (invalid) {
      return;
    }
    const params = new HttpParams().set('username', this.username);
    this.userService.forgotPassword(params).subscribe(
      success => {
        console.log('TCL: ForgotPasswordComponent -> sendLinkForPwordReset -> success', success);
        if (success.success === 0) {
          this.error = { show: true, type: 'danger', msg: success.message };
        } else if (success.success === 1) {
          this.resend = true;
        }
      },
      error => {
        console.log('TCL: ForgotPasswordComponent -> sendLinkForPwordReset -> error', error);
      }
    );
  }
}
