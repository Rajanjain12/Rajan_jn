import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountRoutingModule } from './account-routing.module';
import { ProfileComponent } from './profile/profile.component';
import { FormsModule } from '@angular/forms';
import { CoreModule } from '../core/core.module';
import { SidebarComponent } from './sidebar/sidebar.component';
import { InvoiceComponent } from './invoice/invoice.component';

@NgModule({
  declarations: [ProfileComponent, SidebarComponent, InvoiceComponent],
  imports: [CommonModule, AccountRoutingModule, FormsModule, CoreModule]
})
export class AccountModule {}
