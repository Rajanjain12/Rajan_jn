import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SignupB2bComponent } from './signup-b2b/signup-b2b.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'signup-b2b',
    pathMatch: 'full'
  },
  {
    path: 'signup-b2b',
    component: SignupB2bComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthB2bRoutingModule {}
