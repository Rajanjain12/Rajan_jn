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

  constructor(private userService: UserService,private router: Router) { }
  email :string ="";
  ngOnInit() {
  }

  sendLinkForPwordReset(invalid){
    if(invalid){
      return;
    }
    var params = new HttpParams()
    .set('email', this.email);
   this.userService.forgotPassword(params).subscribe((res: any) => {
      var respData = res;
      console.log("log== "+JSON.stringify(respData));
      if (respData.success == 1) {  
        this.router.navigateByUrl('/login', { replaceUrl: true });
      } else {
        alert(respData.message);
      }
    });
  }
}
