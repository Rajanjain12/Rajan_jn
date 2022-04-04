import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { AboutComponent } from './about/about.component';
import { HomeComponent } from './home/home.component';

import { AuthGuard } from './core/auth.guard';
import { RegComponent } from './reg/reg.component';
import { ContactComponent } from './contact/contact.component';
import { HelpComponent } from './help/help.component';
import { LogoutComponent } from './logout/logout.component';

import { ForgotComponent } from './forgot/forgot.component';
import { BecomeSellerNavComponent } from './seller/Navigation-Bar/become-seller-nav/become-seller-nav.component';
import { NavBarComponent } from './seller/Navigation-Bar/nav-bar/nav-bar.component';
import { BackgroundComponent } from './seller/background/background.component';
import { BecomeSellerComponent } from './seller/become-seller/become-seller.component';
import { HeaderComponent } from './seller/header/header.component';
import { PostPropertyComponent } from './seller/post-property/post-property.component';
import { PostPropertyPageComponent } from './seller/post-property-page/post-property-page.component';
import { PropertyListingsComponent } from './seller/property-listings/property-listings.component';
import { SearchBarComponent } from './seller/search-bar/search-bar.component';
import { UploadFormComponent } from './uploads/upload-form/upload-form.component';
import { ServicePageComponent } from './seller/services/service-page/service-page.component';
import { ViewResponsesComponent } from './seller/services/view-responses/view-responses.component'
import { EditServicesComponent } from './seller/services/edit-services/edit-services.component';
import { BuyerDashboardComponent } from './buyer/buyer-dashboard/buyer-dashboard.component';
import { BuyerPageComponent } from './buyer/buyer-page/buyer-page.component';
import { GoogleMapComponent } from './google-map/google-map.component';
import { EditComponent } from './seller/edit/edit.component';
import { IntPropComponent } from './int-prop/int-prop.component';
import { EditaserviceComponent } from './seller/services/editaservice/editaservice.component';
import { ProfileComponent } from './seller/profile/profile.component';
import { DashBarComponent } from './seller/dash-bar/dash-bar.component';
import { ServiceSearchComponent } from './service-search/service-search.component';
import { ServiceDashComponent } from './service-dash/service-dash.component';
import {ReportPropertyComponent} from './seller/report-property/report-property.component';
import { ReportServiceComponent } from './report-service/report-service.component';
import { DialogAddComponent} from  './dialog-add/dialog-add.component';
import { DiamobComponent } from './diamob/diamob.component';
import { DialogPComponent } from './dialog-p/dialog-p.component';
import { OpenComponent } from './open/open.component';
const routes: Routes = [

  { path:'header', component: HeaderComponent },
  { path:'open', component: OpenComponent },
  { path:'int-prop', component: IntPropComponent },
  { path: 'background', component: BackgroundComponent },
  { path: 'search-bar', component: SearchBarComponent },
  { path: 'nav-bar', component: NavBarComponent },
  { path: 'buy-dash', component: BuyerDashboardComponent },
  { path: 'dash-bar', component: DashBarComponent },
  { path: 'become-seller', component: BecomeSellerComponent },
  { path: 'become-seller-nav', component: BecomeSellerNavComponent },
  { path: 'post-property', component: PostPropertyComponent },
  { path: 'property-listings', component: PropertyListingsComponent },
  { path: 'post-property-page', component: PostPropertyPageComponent },
  { path: 'service-page', component: ServicePageComponent },
  { path: 'view-response', component: ViewResponsesComponent },
  { path: 'edit-services', component: EditServicesComponent },
  { path: 'buyer-dashboard', component: BuyerDashboardComponent },
  { path: 'buyer-page', component: BuyerPageComponent },
  { path: 'service-dash', component: ServiceDashComponent },
  { path: 'report', component: ReportPropertyComponent },
  { path: 'reportS', component: ReportServiceComponent },

  {
    path: 'edit/:ids', component: EditComponent 
  },
  {
    path: 'ser-ser', component: ServiceSearchComponent 
  },
  {
    path: 'profile', component: ProfileComponent 
  },
  {
    path: 'editaservice/:ids', component: EditaserviceComponent
  },
{
  path: 'login', component: LoginComponent ,canActivate: [AuthGuard]
},
{
  path:'logout', component:LogoutComponent
},
{
  path:'forgot', component:ForgotComponent
},
{
  path:'google', component:GoogleMapComponent
},
{
  path: 'registration' , component: RegistrationComponent
},
{
  path : 'reg', component:RegComponent
},
{
  path:'contact', component: ContactComponent
},
{
  path: 'help', component: HelpComponent
},
{
  path: 'about' , component: AboutComponent
},
{
  path: 'orm' , component: UploadFormComponent
},
{ path: '', component: HomeComponent   , pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]

})
export class AppRoutingModule { }
export const routingComponents = [LoginComponent,LogoutComponent ,ContactComponent,RegistrationComponent,UploadFormComponent,RegComponent ,AboutComponent, HomeComponent, HelpComponent,ForgotComponent]
