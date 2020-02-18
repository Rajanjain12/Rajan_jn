import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthModule } from './auth/auth.module';
import { FooterComponent } from './shared/layout/footer/footer.component';
import { HeaderComponent } from './shared/layout/header/header.component';
import { CoreModule } from './core/core.module';
import { SharedModule } from './shared/shared.module';
import { LoaderComponent } from './shared/loader/loader.component';
import { WaitlistModule } from './waitlist/waitlist.module';
import { PostLoginHeaderComponent } from './shared/layout/post-login-header/post-login-header.component';
import { GuestModule } from './guest/guest.module';

@NgModule({
  declarations: [AppComponent, FooterComponent, HeaderComponent, LoaderComponent, PostLoginHeaderComponent],
  imports: [BrowserModule, AppRoutingModule, SharedModule, AuthModule, CoreModule, WaitlistModule, GuestModule],
  exports: [],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
