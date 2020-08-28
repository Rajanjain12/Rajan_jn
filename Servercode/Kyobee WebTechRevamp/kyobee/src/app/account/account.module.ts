import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountRoutingModule } from './account-routing.module';
import { ProfileComponent } from './profile/profile.component';
import { FormsModule } from '@angular/forms';
import { CoreModule } from '../core/core.module';
import { SidebarComponent } from './sidebar/sidebar.component';
import { InvoiceComponent } from './invoice/invoice.component';
import { ConfirmEqualValidatorDirective } from './profile/confirm-equal-validator.directive';

@NgModule({
  declarations: [ProfileComponent, SidebarComponent, InvoiceComponent , ConfirmEqualValidatorDirective],
  imports: [CommonModule, AccountRoutingModule, FormsModule, CoreModule]
})
export class AccountModule {}
