import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from 'src/app/core/services/auth.service';
import { DesignService } from 'src/app/core/services/design.service';
import { CommonService } from 'src/app/core/services/common.service';
import { LocalStorage } from '@ngx-pwa/local-storage';

import { User } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-post-login-header',
  templateUrl: './post-login-header.component.html',
  styleUrls: ['./post-login-header.component.scss']
})
export class PostLoginHeaderComponent implements OnInit {
  loadTheme: string;
  headerStyle: string;
  headerStyleSubscription: any;
  tab: number;

  constructor(
    private designService: DesignService,
    private router: Router,
    private authService: AuthService,
    private commonService: CommonService,
    private localStorage: LocalStorage
  ) {
    this.loadTheme = this.designService.getTheme();
    this.headerStyle = designService.headerStyle;
    this.headerStyleSubscription = this.designService.headerStyleChange.subscribe(value => {
      this.headerStyle = value;
    });
  }
  user: User;
  ngOnInit() {
    this.user = this.authService.getUser();
    if (localStorage.getItem('activeTab') !== null) {
      this.tab = Number(localStorage.getItem('activeTab'));
    } else {
      this.tab = 1;
    }
  }

  logout() {
    // Remove logIn flag in authservice
    this.authService.removeLogFlag();
    // Remove data from localStorage when logOut
    this.authService.removeLocalStorageData();
    // Navigate to Url
    this.router.navigate(['/auth/login']);
  }

  // Purpose : For storing active tab state to local storage , so that we can retrieve state on page refresh
  setActiveTab(data) {
    this.tab = data;
    this.commonService.setLocalStorageData('activeTab', data);
  }
}
