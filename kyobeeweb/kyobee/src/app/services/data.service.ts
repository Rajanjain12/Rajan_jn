import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  public subdomain: string;
  public serverUrl: string;
  constructor(private router: Router, private httpClient: HttpClient) { }

  setData(subdomain:string, serverUrl:string){
    this.subdomain = subdomain;
    this.serverUrl = serverUrl;
  }
  getData(){
    return {
      "subdomain": this.subdomain,
      "serverUrl": this.serverUrl
    }
  }

  changeView(path:string){
    this.router.navigate([path]);
  }
}
