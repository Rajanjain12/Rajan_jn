import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FooterComponent } from './layout/footer/footer.component';
import { HeaderComponent } from './layout/header/header.component';
import { ThankyouComponent } from './thankyou/thankyou.component';



@NgModule({
  declarations: [FooterComponent, HeaderComponent, ThankyouComponent],
  imports: [
    CommonModule
  ]
})
export class SharedModule { }
