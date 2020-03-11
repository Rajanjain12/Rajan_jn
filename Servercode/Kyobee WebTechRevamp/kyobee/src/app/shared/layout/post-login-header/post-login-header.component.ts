import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-post-login-header',
  templateUrl: './post-login-header.component.html',
  styleUrls: ['./post-login-header.component.scss']
})
export class PostLoginHeaderComponent implements OnInit {
  constructor( private router: Router, private authService: AuthService) {}

  ngOnInit() {}

  logout(){
    this.authService.removeLoginData();
    this.router.navigateByUrl('/auth/login', { replaceUrl: true });
  }
}
