import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { AuthB2bRoutingModule } from './auth-b2b-routing.module';
import { SignupB2bComponent } from './signup-b2b/signup-b2b.component';
import { AutocompleteLibModule } from 'angular-ng-autocomplete';
import { RegisterBusinessComponent } from './register-business/register-business.component';
import { CreateBusinessComponent } from './create-business/create-business.component';
import { SelectPlanComponent } from './select-plan/select-plan.component';

@NgModule({
  declarations: [SignupB2bComponent, RegisterBusinessComponent, CreateBusinessComponent, SelectPlanComponent],
  imports: [CommonModule, AuthB2bRoutingModule, AutocompleteLibModule, FormsModule]
})
export class AuthB2bModule {}
