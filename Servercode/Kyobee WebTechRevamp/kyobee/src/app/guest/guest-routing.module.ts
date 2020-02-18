import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { GuestDetailUpdateComponent } from './guest-detail-update/guest-detail-update.component';

const routes: Routes = [
  {
    path: 'guest/guest-detail/s/:id',
    component: GuestDetailUpdateComponent,
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GuestRoutingModule {}
