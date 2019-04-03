import { Injectable, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class DataService{
  public subdomain: string;
  public serverUrl: string;
  constructor(private httpClient: HttpClient) { }

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
}
