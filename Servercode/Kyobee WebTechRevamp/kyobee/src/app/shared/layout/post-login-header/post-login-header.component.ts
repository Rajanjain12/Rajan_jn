import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from 'src/app/core/services/auth.service';
import { DesignService } from 'src/app/core/services/design.service';

import { User } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-post-login-header',
  templateUrl: './post-login-header.component.html',
  styleUrls: ['./post-login-header.component.scss']
})
export class PostLoginHeaderComponent implements OnInit {
  loadTheme: string;

  constructor(private designService: DesignService, private router: Router, private authService: AuthService) {
    this.loadTheme = this.designService.getTheme();
  }
  user: User;
  ngOnInit() {
    this.user = this.authService.getUser();
  }

  logout() {
    this.authService.removeLoginData();
    this.router.navigate(['/auth/login']);
  }
}
