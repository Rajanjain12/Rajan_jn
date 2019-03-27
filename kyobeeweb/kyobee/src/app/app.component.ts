import { Component, OnInit } from '@angular/core';
import { DataService } from './services/data.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  public loading: boolean = false;
  public url: string;
  public logoImgSrc: string;
  public cssFile: string;
  public faciconLogoSrc: string;
  public title: string;
  public subdomain: string;
  public serverUrl: string;

  constructor(private _dataService: DataService){}

  ngOnInit(){
    this.url = window.location.href;

    // Loading resrouces based on url 
    var url = window.location.href;
    var initials = url.split(".")[0];
    var subdomain = initials.split("//")[1];
    
    if(subdomain.includes("localhost:4200")){
      this.cssFile = "../assets/css/theme-admin.css";
      this.logoImgSrc = "../assets/img/adminlogo.png";
      this.faciconLogoSrc = "../assets/img/favicon-admin.png";
      this.serverUrl = "http://localhost:8080";
      this.title = "Kyobee";
      this.subdomain = "admin";
    } else {
      if(subdomain != null && subdomain != 'undefined'){
          this.cssFile = "../assets/css/theme-" + subdomain + ".css";
          this.logoImgSrc = "../assets/img/" + subdomain + "logo.png";
          this.faciconLogoSrc = "../assets/img/favicon-" + subdomain + ".png";
          this.subdomain = subdomain;
          
          if(subdomain == 'admin'){
            this.title = "Kyobee";		
          } else {
            this.title = subdomain;	
          }
        } else {
          this.cssFile = "../assets/css/theme-admin.css";
          this.logoImgSrc = "../assets/img/adminlogo.png";
          this.faciconLogoSrc = "../assets/img/favicon-admin.png";
          this.title = "Kyobee";
          this.subdomain = subdomain;
        }	
    }
    
    console.log(this.cssFile);
    console.log(this.logoImgSrc);
    document.write('<link rel="stylesheet" href="' + this.cssFile +'" />');
    document.write('<link rel="icon" type="image/png" href="' + this.faciconLogoSrc + '">');
    document.write('<title>'+ this.title +'</title>');
    this._dataService.setData(this.subdomain, this.serverUrl);
  }
}
export const imgLinks = { "signinPageImageSrc": "../assets/img/sign-in-illu.png",
                          "forgotPwdPageImageSrc":"../assets/img/recovery-pass-illu.png",
                          "correctSignImageSrc":"../assets/img/correct-sign.png",
                          "resetPwdPageImageSrc":"../assets/img/reset-pass-illu.png"}
