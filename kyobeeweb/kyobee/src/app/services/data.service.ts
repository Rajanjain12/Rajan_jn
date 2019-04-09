import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpService } from './http.service';

@Injectable({
  providedIn: 'root'
})

export class DataService{
  public subdomain: string;
  constructor(private httpClient: HttpClient) { }

  setData(subdomain:string){
    this.subdomain = subdomain;
  }
  getData(){
    return {
      "subdomain": this.subdomain
    }
  }
}
