import { Component, OnInit } from '@angular/core';
import { AuthService } from '../core/auth.service';
import { Router, Params } from '@angular/router';


@Component({
  selector: 'app-buy-nav',
  templateUrl: './buy-nav.component.html',
  styleUrls: ['./buy-nav.component.css']
})
export class BuyNavComponent implements OnInit {

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
