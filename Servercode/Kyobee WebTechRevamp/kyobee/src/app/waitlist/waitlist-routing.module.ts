import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { MainpageComponent } from './mainpage/mainpage.component';
import { AddGuestComponent } from './add-guest/add-guest.component';
import { SettingsComponent } from './settings/settings.component';
import { HistoryComponent } from './history/history.component';
import { AuthGuardGuard } from '../core/auth-guard.guard';

const routes: Routes = [
  {
    path: '',
    component: MainpageComponent, 
    canActivate:[AuthGuardGuard],
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
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WaitlistRoutingModule {}
