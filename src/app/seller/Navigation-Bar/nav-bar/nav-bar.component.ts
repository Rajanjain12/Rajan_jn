import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../core/auth.service';
import { Router, Params } from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  
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
