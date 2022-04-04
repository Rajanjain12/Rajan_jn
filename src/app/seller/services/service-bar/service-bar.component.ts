import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../core/auth.service';
import { Router, Params } from '@angular/router';

@Component({
  selector: 'app-service-bar',
  templateUrl: './service-bar.component.html',
  styleUrls: ['./service-bar.component.css']
})
export class ServiceBarComponent implements OnInit {

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
      // this.errorMessage = err.message;
    })
  }

}
