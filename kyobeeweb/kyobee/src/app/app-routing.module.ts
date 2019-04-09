import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './components/public/login/login.component';
import { SignupComponent } from './components/public/signup/signup.component';
import { ForgotpwdComponent } from './components/public/forgotpwd/forgotpwd.component';
import { ResetpwdComponent } from './components/public/resetpwd/resetpwd.component';
import { ResetpwdThanksComponent } from './components/public/resetpwd-thanks/resetpwd-thanks.component';
import { OrgProfileComponent } from './components/web/org-profile/org-profile.component';
import { DashboardComponent } from './components/web/dashboard/dashboard.component';
import { HistoryComponent } from './components/web/history/history.component';

const routes: Routes = [
  { path: '', redirectTo: 'public', pathMatch:'full'},
  { path: 'public', children: [
    { path: '', redirectTo: 'login', pathMatch:'full'},
    { path: 'login', component: LoginComponent },
    { path: 'dashboard', redirectTo: '/web/wishlist/home', pathMatch: 'full' },
    { path: 'signup', component: SignupComponent},
    { path: 'forgotpwd', component: ForgotpwdComponent},
    { path: 'resetpwd/:userId/:authcode', component: ResetpwdComponent},
    { path: 'resetpwdthanks', component: ResetpwdThanksComponent},
    { path: '**', redirectTo: 'login', pathMatch: 'full'}
  ]},
  { path: 'web', children: [
    { path: '', redirectTo: 'wishlist', pathMatch:'full'},
    { path: 'wishlist', children:[
      { path: '', redirectTo: 'home', pathMatch:'full'},
      { path: 'home', component: DashboardComponent},
      { path: 'history',component: HistoryComponent },
      { path: '**', redirectTo: 'wishlist', pathMatch: 'full'}
    ]},
    { path: 'myaccount', children: [
      { path: 'profile', component: OrgProfileComponent}
    ]}
  ]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routerComponents =  [LoginComponent, OrgProfileComponent, DashboardComponent, HistoryComponent, SignupComponent, ForgotpwdComponent, ResetpwdComponent, ResetpwdThanksComponent]