import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { GuestRoutingModule } from './guest-routing.module';
import { PubNubAngular } from 'pubnub-angular2';
import { GuestDetailUpdateComponent } from './guest-detail-update/guest-detail-update.component';
import { MaxValidator } from './guest-detail-update/max.validator';
import { Min_MaxValidatorDirective } from './guest-detail-update/min.validator';

@NgModule({
  declarations: [GuestDetailUpdateComponent, MaxValidator, Min_MaxValidatorDirective],
  imports: [CommonModule, GuestRoutingModule, FormsModule],
  providers: [PubNubAngular]
})
export class GuestModule {}
