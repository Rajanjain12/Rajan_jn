import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  public subdomain: string;
  constructor(private router: Router, private httpClient: HttpClient) { }

  setData(subdomain:string){
    this.subdomain = subdomain;
  }
  getData(){
    return {
      "subdomain": this.subdomain
    }
  }

  getImgLinkData(){
    return this.httpClient.get(dataObjectLink.imgLinkData);
  }

  changeView(path:string){
    this.router.navigate([path]);
  }
}
