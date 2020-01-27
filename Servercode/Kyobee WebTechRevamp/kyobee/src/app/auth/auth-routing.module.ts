import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SigninComponent } from './signin/signin.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { SignupComponent } from './signup/signup.component';


const routes: Routes = [ {
  path: 'login',
  component: SigninComponent,
},
{
  path: '',
  component: SigninComponent,
},{
  path:'forgot-password',
  component:ForgotPasswordComponent
},
{
  path: 'signup',
  component: SignupComponent
},
{
  path:'reset-password/:userId/:authCode',
  component:ResetPasswordComponent
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
