import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GuestService {
  constructor(private apiService: ApiService) {}

  resetUser(orgid: HttpParams) {
    return this.apiService.getParam('rest/waitlist/guest/resetGuestList', orgid);
  }

  fetchGuestList(params) {
    return this.apiService.getParam('rest/waitlist/guest/', params);
  }

  addGuest(serviceResult: object) {
    return this.apiService.post('rest/waitlist/guest/', serviceResult);
  }

  fetchGuest(id) {
    return this.apiService.get('rest/waitlist/guest/' + id);
  }

  fetchGuestDetail(uuid) {
    return this.apiService.get('/rest/waitlist/guest/uuid/' + uuid);
  }

  updateGuest(body) {
    return this.apiService.put('rest/waitlist/guest/', body);
  }

  updateGuestStatus(params) {
    return this.apiService.putParams('rest/waitlist/guest/status', params);
  }

  fetchGuestHistoryList(params) {
    return this.apiService.getParam('rest/waitlist/guest/fetchGuestHistory', params);
  }

  fetchGuestMetrics(params) {
    return this.apiService.getParam('rest/waitlist/guest/metrics', params);
  }
  fetchOrgPrefandKeyMap(params) {
    return this.apiService.getParam('rest/waitlist/orgPrefAndKeyMap', params);
  }
  sendSMS(params) {
    return this.apiService.post('rest/waitlist/sendSMS', params);
  }
}