import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgxMaskModule } from 'ngx-mask';

import { AuthRoutingModule } from './auth-routing.module';

import { ConfirmEqualValidatorDirective } from './signup/confirm-equal-validator.directive';

import { SigninComponent } from './signin/signin.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { SignupComponent } from './signup/signup.component';

@NgModule({
  declarations: [
    SigninComponent,
    ForgotPasswordComponent,
    SignupComponent,
    ConfirmEqualValidatorDirective,
    ResetPasswordComponent
  ],
  imports: [CommonModule, AuthRoutingModule, FormsModule, NgxMaskModule.forRoot()]
})
export class AuthModule {}
