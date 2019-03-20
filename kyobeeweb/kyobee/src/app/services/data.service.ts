import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  public subdomain: string;
  constructor(private router: Router) { }

  setData(subdomain:string){
    this.subdomain = subdomain;
  }
  getData(){
    return {
      "subdomain": this.subdomain
    }
  }

  changeView(path:string){
    alert("abt to navigate");
    this.router.navigate([path]);
  }
}
