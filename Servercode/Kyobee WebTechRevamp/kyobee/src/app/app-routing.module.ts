import { NgModule } from '@angular/core';
import { Routes, RouterModule, PreloadAllModules } from '@angular/router';

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'auth-b2b',
    loadChildren: () => import('./auth-b2b/auth-b2b.module').then(m => m.AuthB2bModule)
  },
  {
    path: 'waitlist',
    loadChildren: () => import('./waitlist/waitlist.module').then(m => m.WaitlistModule)
  },
  {
    path: 'guest',
    loadChildren: () => import('./guest/guest.module').then(m => m.GuestModule)
  },
  {
    path: 'dashboard',
    loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule)
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'auth/login'
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: 'auth/login'
    // TODO: Please add page not found here, as of now we are redirecting to login page
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      preloadingStrategy: PreloadAllModules
    })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
