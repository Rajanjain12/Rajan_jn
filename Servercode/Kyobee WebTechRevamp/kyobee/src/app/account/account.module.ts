import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountRoutingModule } from './account-routing.module';
import { ProfileComponent } from './profile/profile.component';
import { FormsModule } from '@angular/forms';
import { CoreModule } from '../core/core.module';


@NgModule({
  declarations: [ProfileComponent],
  imports: [
    CommonModule,
    AccountRoutingModule,
    FormsModule,
    CoreModule
  ]
})
export class AccountModule { }
