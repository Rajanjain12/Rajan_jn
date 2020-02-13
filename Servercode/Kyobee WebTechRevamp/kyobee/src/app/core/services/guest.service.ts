import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GuestService {

  constructor(private apiService: ApiService) { }

  resetUser(orgid: HttpParams){
    return this.apiService.getParam('rest/waitlist/guest/resetGuestList', orgid);
  }

  fetchGuestList(params){
    return this.apiService.getParam('rest/waitlist/guest/', params);
  }

  addGuest(serviceResult: object){
    return this.apiService.post('rest/waitlist/guest/', serviceResult);
  }
}
