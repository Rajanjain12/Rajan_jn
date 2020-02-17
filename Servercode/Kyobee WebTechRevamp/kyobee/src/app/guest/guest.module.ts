import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { GuestRoutingModule } from './guest-routing.module';
import { AuthRoutingModule } from '../auth/auth-routing.module';
import { GuestDetailUpdateComponent } from './guest-detail-update/guest-detail-update.component';

@NgModule({
  declarations: [GuestDetailUpdateComponent],
  imports: [CommonModule, GuestRoutingModule, FormsModule]
})
export class GuestModule {}
