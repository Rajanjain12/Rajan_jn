import { Component, OnInit } from '@angular/core';
import { AuthService } from '../core/auth.service';
import { Router, Params } from '@angular/router';
@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent  {
  errorMessage: string = '';

  constructor(public authService: AuthService,private router: Router,) {

   }
   
  tryLogout(){
    this.authService.doLogout()
    .then(res => {
      this.router.navigate(['']);
    }, err => {
      console.log(err);
      this.errorMessage = err.message;
    })
  }

  // ngOnInit() {
  // }

}
