import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { HeaderComponent } from './layout/header/header.component';
import { FooterComponent } from './layout/footer/footer.component';
import { PostLoginHeaderComponent } from './layout/post-login-header/post-login-header.component';
import { ThankyouComponent } from './thankyou/thankyou.component';
import { LoaderComponent } from './loader/loader.component';

@NgModule({
  declarations: [ThankyouComponent, HeaderComponent, PostLoginHeaderComponent, LoaderComponent, FooterComponent],
  imports: [CommonModule, RouterModule],
  exports: [ThankyouComponent, HeaderComponent, PostLoginHeaderComponent, LoaderComponent, FooterComponent]
})
export class SharedModule {}
