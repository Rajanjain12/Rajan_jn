import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';
import { WaitlistRoutingModule } from './waitlist-routing.module';
import { SidebarComponent } from './sidebar/sidebar.component';
import { MainpageComponent } from './mainpage/mainpage.component';
import { AddGuestComponent } from './add-guest/add-guest.component';
import { HistoryComponent } from './history/history.component';
import { SettingsComponent } from './settings/settings.component';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [DashboardComponent, SidebarComponent, MainpageComponent, AddGuestComponent, HistoryComponent, SettingsComponent],
  imports: [
    CommonModule,
    WaitlistRoutingModule,
    FormsModule
  ]
})
export class WaitlistModule { }
