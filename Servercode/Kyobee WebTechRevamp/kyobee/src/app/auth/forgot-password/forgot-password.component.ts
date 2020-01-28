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
  username :string ="";
  show: boolean=null;
  ngOnInit() {
  }

  sendLinkForPwordReset(invalid){
    if(invalid){
      return;
    }
    var params = new HttpParams()
    .set('username', this.username);
   this.userService.forgotPassword(params).subscribe((res: any) => {
      var respData = res;
  
      if (respData.success == 1) {  
        alert();
        this.show=true;
       
      } else {
        this.show = false;
        
      }
    });
  }
}
