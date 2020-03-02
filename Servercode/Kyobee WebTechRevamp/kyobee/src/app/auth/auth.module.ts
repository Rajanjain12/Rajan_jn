import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthRoutingModule } from './auth-routing.module';
import { SigninComponent } from './signin/signin.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { SignupComponent } from './signup/signup.component';
import { ConfirmEqualValidatorDirective } from './signup/confirm-equal-validator.directive';

@NgModule({
  declarations: [
    SigninComponent,
    ForgotPasswordComponent,
    SignupComponent,
    ConfirmEqualValidatorDirective,
    ResetPasswordComponent
  ],
  imports: [CommonModule, AuthRoutingModule, FormsModule]
})
export class AuthModule {}
