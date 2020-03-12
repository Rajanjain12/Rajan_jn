import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { GuestRoutingModule } from './guest-routing.module';
import { PubNubAngular } from 'pubnub-angular2';
import { GuestDetailUpdateComponent } from './guest-detail-update/guest-detail-update.component';
/* import { MaxValidator } from '../core/directives/max.validator';
import { MinValidator } from '../core/directives/min.validator'; */

@NgModule({
  declarations: [GuestDetailUpdateComponent],
  imports: [CommonModule, GuestRoutingModule, FormsModule],
  providers: [PubNubAngular]
})
export class GuestModule {}
