import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GuestService {

  constructor(private apiService: ApiService) { }

  resetUser(orgid: HttpParams){
    return this.apiService.get('http://localhost:8082/rest/guest/resetGuestList', orgid);
  }
  fetchGuestList(params){
    return this.apiService.getBodyParam('http://localhost:8082/rest/guest', params);
  }
}
