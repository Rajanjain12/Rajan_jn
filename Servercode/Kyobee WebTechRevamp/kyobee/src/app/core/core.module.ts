import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AuthService } from './services/auth.service';
import { UserService } from './services/user.service';
import { ApiService } from './services/api.service';
import { LoaderService } from './services/loader.service';
import { LoaderInterceptor } from './interceptors/loader.interceptor';
import { AutosizeDirective } from './directives/autosize.directive';
import { MaxValidator } from './directives/max.validator';
import { MinValidator } from './directives/min.validator';

@NgModule({
  declarations: [AutosizeDirective, MaxValidator, MinValidator],
  imports: [CommonModule, HttpClientModule],
  providers: [
    UserService,
    ApiService,
    AuthService,
    LoaderService,
    { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true }
  ],
  exports: [AutosizeDirective, MaxValidator, MinValidator]
})
export class CoreModule {}
