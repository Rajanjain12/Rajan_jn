import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {Ng5SliderModule} from 'ng5-slider';
import {DataTablesModule} from 'angular-datatables';
import { NgxMaskModule } from 'ngx-mask';
import { AppRoutingModule, routerComponents } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './components/common/footer/footer.component';
import { SignupPlanAndPricingComponent } from './components/public/signup-plan-and-pricing/signup-plan-and-pricing.component';
import { SignupVerificationComponent } from './components/public/signup-verification/signup-verification.component';

@NgModule({
  declarations: [
    AppComponent,
    routerComponents,
    FooterComponent,
    SignupPlanAndPricingComponent,
    SignupVerificationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    Ng5SliderModule,
    DataTablesModule,
 NgxMaskModule.forRoot()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
