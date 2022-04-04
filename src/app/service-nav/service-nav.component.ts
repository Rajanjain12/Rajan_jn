import { Component, OnInit } from '@angular/core';

import { AuthService } from '../core/auth.service';
import { Router, Params } from '@angular/router';

@Component({
  selector: 'app-service-nav',
  templateUrl: './service-nav.component.html',
  styleUrls: ['./service-nav.component.css']
})
export class ServiceNavComponent implements OnInit {

  errorMessage: string = '';
  constructor(public authService: AuthService,private router: Router,) { }

  ngOnInit() {
  }

  tryLogout(){
    this.authService.doLogout()
    .then(res => {
      this.router.navigate(['']);
    }, err => {
      
      this.errorMessage = err.message;
      console.log(this.errorMessage);
    })
  }

}
