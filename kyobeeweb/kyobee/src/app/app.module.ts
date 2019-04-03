import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {Ng5SliderModule} from 'ng5-slider';
import {DataTablesModule} from 'angular-datatables';

import { AppRoutingModule, routerComponents } from './app-routing.module';
import { AppComponent } from './app.component';
import { OrgProfileComponent } from './web/org-profile/org-profile.component';

@NgModule({
  declarations: [
    AppComponent,
    routerComponents,
    OrgProfileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    Ng5SliderModule,
    DataTablesModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
