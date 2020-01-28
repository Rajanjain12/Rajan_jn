import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThankyouComponent } from './thankyou/thankyou.component';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [ ThankyouComponent],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports:[
    ThankyouComponent
  ]
})
export class SharedModule { }
