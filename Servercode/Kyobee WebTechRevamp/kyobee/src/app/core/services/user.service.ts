import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import {  HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private apiService: ApiService) { }

  login(request : object) {
    return this.apiService.post('rest/user/login',request);
   }
   forgotPassword(email : HttpParams){
    return this.apiService.postParams('rest/user/forgotPassword',email);
   }
}
