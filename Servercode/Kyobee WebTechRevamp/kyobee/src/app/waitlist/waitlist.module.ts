import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PubNubAngular } from 'pubnub-angular2';
import { Ng5SliderModule } from 'ng5-slider';
import { NgxMaskModule } from 'ngx-mask';

import { CoreModule } from '../core/core.module';
import { WaitlistRoutingModule } from './waitlist-routing.module';

import { DashboardComponent } from './dashboard/dashboard.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { MainpageComponent } from './mainpage/mainpage.component';
import { AddGuestComponent } from './add-guest/add-guest.component';
import { HistoryComponent } from './history/history.component';
import { SettingsComponent } from './settings/settings.component';
import { MaxValidator } from '../core/directives/max.validator'
/* import { MinValidator } from '../core/directives/min.validator'; */

@NgModule({
  declarations: [
    DashboardComponent,
    SidebarComponent,
    MainpageComponent,
    AddGuestComponent,
    HistoryComponent,
    SettingsComponent,
   
      /*  MaxValidator, */
 /* MinValidator */
  ],
  imports: [CommonModule, CoreModule, WaitlistRoutingModule, FormsModule, Ng5SliderModule, NgxMaskModule.forRoot()],
  providers: [PubNubAngular]
})
export class WaitlistModule {}
