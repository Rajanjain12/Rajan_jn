import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { MainpageComponent } from './mainpage/mainpage.component';
import { AddGuestComponent } from './add-guest/add-guest.component';
import { SettingsComponent } from './settings/settings.component';
import { HistoryComponent } from './history/history.component';

const routes: Routes = [
{
  path: 'waitlist',
  component: MainpageComponent,
  children: [
{
  path: 'dashboard',
 component: DashboardComponent
},
{
  path: 'addguest',
 component: AddGuestComponent
},
{
  path: 'addguest/:id',
 component: AddGuestComponent
},
{
  path: 'history',
 component: HistoryComponent
},
{
  path: 'settings',
 component: SettingsComponent
} ] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WaitlistRoutingModule { }
