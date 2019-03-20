import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './public/login/login.component';
import { WaitlistComponent } from './web/waitlist/waitlist.component';
import { SignupComponent } from './public/signup/signup.component';
import { ForgotpwdComponent } from './public/forgotpwd/forgotpwd.component';

const routes: Routes = [
  { path: '', redirectTo: 'public', pathMatch:'full'},
  { path: 'public', children: [
    { path: '', redirectTo: 'login', pathMatch:'full'},
    { path: 'login', component: LoginComponent },
    { path: 'dashboard', redirectTo: '/web/home', pathMatch: 'full' },
    { path: 'signup', component: SignupComponent},
    { path: 'forgotpwd', component: ForgotpwdComponent},
    { path: '**', redirectTo: 'login', pathMatch: 'full'}
  ]},
  { path: 'web', children: [
    { path: '', redirectTo: 'home', pathMatch:'full'},
    { path: 'home', component: WaitlistComponent }
  ]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routerComponents =  [LoginComponent, WaitlistComponent, SignupComponent, ForgotpwdComponent]