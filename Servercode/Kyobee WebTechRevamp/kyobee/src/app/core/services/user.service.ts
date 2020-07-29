import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  resetPwd(request: object) {
    return this.apiService.post('rest/user/resetPassword', request);
  }

  constructor(private apiService: ApiService) {}

  login(request: object) {
    return this.apiService.post('rest/user/login', request);
  }
  forgotPassword(username: HttpParams) {
    return this.apiService.postParams('rest/user/forgotPassword', username);
  }
  fetchCountryList() {
    return this.apiService.get('rest/user/country');
  }
  fetchLatLon(zipCode: HttpParams) {
    return this.apiService.getParams('rest/user/latLon', zipCode);
  }
  fetchPlaceList(params) {
    return this.apiService.getParams('rest/user/placeList/', params);
  }
  fetchPlaceDetails(placeId: HttpParams) {
    return this.apiService.getParams('rest/user/placeDetails', placeId);
  }
  saveUser(request) {
    return this.apiService.post('rest/user/user', request);
  }
  activateUser(params) {
    return this.apiService.postParams('rest/user/activateUser', params);
  }
  resendCode(userId: HttpParams) {
    return this.apiService.postParams('rest/user/resendCode', userId);
  }
  validateResetPwdUrl(params) {
    return this.apiService.getParams('rest/user/validateResetPwdUrl/', params);
  }
  fetchOrganization(orgId: HttpParams) {
    return this.apiService.getParams('rest/user/organization', orgId);
  }
  changePassword(params) {
    return this.apiService.putParams('rest/user/password', params);
  }
  updateProfileSetting(params) {
    return this.apiService.postFile('rest/user/profileSetting', params);
  }
}
