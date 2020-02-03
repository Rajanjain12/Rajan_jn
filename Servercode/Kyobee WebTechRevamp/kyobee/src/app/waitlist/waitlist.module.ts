import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';
import { WaitlistRoutingModule } from './waitlist-routing.module';



@NgModule({
  declarations: [DashboardComponent],
  imports: [
    CommonModule,
    WaitlistRoutingModule
  ]
})
export class WaitlistModule { }
