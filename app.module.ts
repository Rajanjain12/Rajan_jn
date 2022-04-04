import { BrowserModule } from '@angular/platform-browser';
import { NgModule, Component } from '@angular/core';
import { AngularFireModule } from '@angular/fire';
import { AppRoutingModule , routingComponents } from './app-routing.module';
import { AppComponent } from './app.component';
import { environment } from 'src/environments/environment';
import * as firebase from 'firebase';
import {  AngularFireDatabaseModule } from '@angular/fire/database';
import { FormsModule,  ReactiveFormsModule} from '@angular/forms';
import * as http from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
// import{ HttpModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { AuthService } from './core/auth.service';
import { UserService } from './core/user.service';
import { AgmCoreModule } from '@agm/core';
import { AuthGuard } from './core/auth.guard';
import { AngularFireAuth, AngularFireAuthModule } from '@angular/fire/auth';
import { pipe } from 'rxjs';
import { AngularFirestore, AngularFirestoreModule } from '@angular/fire/firestore';
import { LoginComponent } from './login/login.component';
import { RegComponent } from './reg/reg.component';
import { ContactComponent } from './contact/contact.component';
import { HelpComponent } from './help/help.component';
import { LogoutComponent } from './logout/logout.component';


import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

import { ForgotComponent } from './forgot/forgot.component';
import { AutocompleteComponent1 } from './google-places1.component';
import { AutocompleteComponent } from './google-places.component';
import { BecomeSellerNavComponent } from './seller/Navigation-Bar/become-seller-nav/become-seller-nav.component';
import { NavBarComponent } from './seller/Navigation-Bar/nav-bar/nav-bar.component';
import { BackgroundComponent } from './seller/background/background.component';
import { BecomeSellerComponent } from './seller/become-seller/become-seller.component';
import { HeaderComponent } from './seller/header/header.component';
import { PostPropertyComponent } from './seller/post-property/post-property.component';
import { PostPropertyPageComponent } from './seller/post-property-page/post-property-page.component';
import { PropertyListingsComponent } from './seller/property-listings/property-listings.component';
import { SearchBarComponent } from './seller/search-bar/search-bar.component';
import { UploadListComponent } from './uploads/upload-list/upload-list.component';
import { UploadFormComponent } from './uploads/upload-form/upload-form.component';
import { MainNavigationComponent } from './main-navigation/main-navigation.component';
import { ServiceBarComponent } from './seller/services/service-bar/service-bar.component';
import { ServicePageComponent } from './seller/services/service-page/service-page.component';
import { ViewResponsesComponent } from './seller/services/view-responses/view-responses.component';
import { EditServicesComponent } from './seller/services/edit-services/edit-services.component';
import { BuyerDashboardComponent } from './buyer/buyer-dashboard/buyer-dashboard.component';
import { BuyerPageComponent } from './buyer/buyer-page/buyer-page.component';
import { ReportPropertyComponent } from './seller/report-property/report-property.component';
import { GoogleMapComponent } from './google-map/google-map.component';
import { ObjNgFor } from './ObjNgFor.pipe';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSliderModule } from '@angular/material/slider';
import { EditComponent } from './seller/edit/edit.component';
import { EditaserviceComponent } from './seller/services/editaservice/editaservice.component';
import { NgxUsefulSwiperModule } from 'ngx-useful-swiper';
import { ProfileComponent } from './seller/profile/profile.component';
import { DashBarComponent } from './seller/dash-bar/dash-bar.component';
import { BuyNavComponent } from './buy-nav/buy-nav.component';
import { ServiceSearchComponent } from './service-search/service-search.component';
import { ServiceNavComponent } from './service-nav/service-nav.component';
import { ServiceDashComponent } from './service-dash/service-dash.component';

import { IntPropComponent } from './int-prop/int-prop.component';
import { ReportServiceComponent } from './report-service/report-service.component';
import { DialogComponent } from './dialog/dialog.component';
import { DialogPComponent } from './dialog-p/dialog-p.component';
import { DialogAddComponent } from './dialog-add/dialog-add.component';
import { OpenComponent } from './open/open.component';
import { DialogIntComponent } from './dialog-int/dialog-int.component';
import { DialogSerComponent } from './dialog-ser/dialog-ser.component';
import { DiamobComponent } from './diamob/diamob.component';
import { DialoginComponent } from './dialogin/dialogin.component';
import { DiamsgComponent } from './diamsg/diamsg.component';
import { DiaIntComponent } from './dia-int/dia-int.component';
import { DialogViewResponsesComponent } from './dialog-view-responses/dialog-view-responses.component';
import { DialogVResServiceComponent } from './dialog-v-res-service/dialog-v-res-service.component';
import { DiaviewreportComponent } from './diaviewreport/diaviewreport.component';
import { DiaPropReportComponent } from './dia-prop-report/dia-prop-report.component';
import { DiaforinterestedComponent } from './diaforinterested/diaforinterested.component';
@NgModule({
  declarations: [
    AppComponent,
    AutocompleteComponent,
    AutocompleteComponent1,
    routingComponents,
    ReportPropertyComponent,
    LogoutComponent,
    ObjNgFor,
    ForgotComponent,
    HeaderComponent,
    NavBarComponent,
    BackgroundComponent,
    SearchBarComponent,
    BecomeSellerComponent,
    BecomeSellerNavComponent,
    PropertyListingsComponent,
    PostPropertyComponent,
    PostPropertyPageComponent,
    UploadListComponent,
    UploadFormComponent,
    MainNavigationComponent,
    ServiceBarComponent,
    ServicePageComponent,
    ViewResponsesComponent,
    EditServicesComponent,
    BuyerDashboardComponent,
    BuyerPageComponent,
    ReportPropertyComponent,
    GoogleMapComponent,
    EditComponent,
    EditaserviceComponent,
    ProfileComponent,
    DashBarComponent,
    BuyNavComponent,
    ServiceSearchComponent,
    ServiceNavComponent,
    ServiceDashComponent,
    IntPropComponent,
    ReportServiceComponent,
    DialogComponent,
    DialogPComponent,
    DialogAddComponent,
    OpenComponent,
    DialogIntComponent,
    DialogSerComponent,
    DiamobComponent,
    DialoginComponent,
    DiamsgComponent,
    DiaIntComponent,
    DialogViewResponsesComponent,
    DialogVResServiceComponent,
    DiaviewreportComponent,
    DiaPropReportComponent,
    DiaforinterestedComponent
  ],
  entryComponents: [
    DialogComponent,
    DiaPropReportComponent,
    DiaviewreportComponent,
    DialogPComponent,
    DialogAddComponent,
    DialogIntComponent,
    DialogSerComponent,
    DiamobComponent,
    DialogVResServiceComponent,
    DialogViewResponsesComponent,
    DialoginComponent,
    DiamsgComponent,
    DiaIntComponent,
    DiaforinterestedComponent
    
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFireDatabaseModule,
    FormsModule,
    AngularFireAuthModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatSliderModule,
    AngularFirestoreModule.enablePersistence(),
    AgmCoreModule.forRoot({
    apiKey: environment.firebase.googleMapsKey
    }),
    BrowserAnimationsModule,
    NgxUsefulSwiperModule ,
    MatButtonModule,
    MatDialogModule,
  
  ],
  providers: [AuthGuard,AutocompleteComponent,AngularFirestoreModule,AngularFirestore,UserService,AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
