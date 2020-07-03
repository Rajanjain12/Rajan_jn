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
import { NumberDirective } from './directives/numbers-only.directive';
import { WhiteSpaceRestrictionDirective } from './directives/whitespace-restriction.directive';

@NgModule({
  declarations: [AutosizeDirective, MaxValidator, MinValidator, NumberDirective, WhiteSpaceRestrictionDirective],
  imports: [CommonModule, HttpClientModule],
  providers: [
    UserService,
    ApiService,
    AuthService,
    LoaderService,
    { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true }
  ],
  exports: [AutosizeDirective, MaxValidator, MinValidator, NumberDirective, WhiteSpaceRestrictionDirective]
})
export class CoreModule {}
