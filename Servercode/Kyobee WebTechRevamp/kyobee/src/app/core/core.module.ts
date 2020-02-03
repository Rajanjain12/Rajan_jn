import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AuthService } from './services/auth.service';
import { UserService } from './services/user.service';
import { ApiService } from './services/api.service';
import { LoaderService } from './services/loader.service';
import { LoaderInterceptor } from './interceptors/loader.interceptor';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HttpClientModule
  ],
  providers: [
    UserService,
    ApiService,
    AuthService,
    LoaderService, { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true }
  ]
})
export class CoreModule { }