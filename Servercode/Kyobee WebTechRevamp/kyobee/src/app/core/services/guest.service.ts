import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GuestService {
  constructor(private apiService: ApiService) {}

  resetUser(orgid: HttpParams) {
    return this.apiService.getParams('rest/waitlist/guest/resetGuestList', orgid);
  }

  fetchGuestList(params) {
    return this.apiService.getParams('rest/waitlist/guest/', params);
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
    return this.apiService.getParams('rest/waitlist/guest/fetchGuestHistory', params);
  }

  fetchGuestMetrics(params) {
    return this.apiService.getParams('rest/waitlist/guest/metrics', params);
  }
  fetchOrgPref(params) {
    return this.apiService.getParams('rest/waitlist/orgPref', params);
  }
  sendSMS(params) {
    return this.apiService.post('rest/waitlist/sendSMS', params);
  }
  fetchLanguageKeyMap(params) {
    return this.apiService.getParams('rest/waitlist/orgLangKeyMap', params);
  }
}
