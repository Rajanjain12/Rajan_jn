import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../core/auth.service';
import { Router, Params } from '@angular/router';

@Component({
  selector: 'app-become-seller-nav',
  templateUrl: './become-seller-nav.component.html',
  styleUrls: ['./become-seller-nav.component.css']
})
export class BecomeSellerNavComponent implements OnInit {

  errorMessage: string = '';
  constructor(public authService: AuthService,private router: Router,) { }

  ngOnInit() {
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

}