import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: 'waitlist',
    loadChildren: './waitlist/waitlist.module#WaitlistModule'
  },
  {
    path: 'guest',
    loadChildren: './guest/guest.module#GuestModule'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
