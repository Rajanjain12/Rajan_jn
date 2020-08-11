import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  constructor(private apiService: ApiService) {}

  fetchAccountDetails(orgId: HttpParams) {
    return this.apiService.getParams('rest/account/', orgId);
  }
  changePassword(params) {
    return this.apiService.putParams('rest/account/password', params);
  }
  updateAccountDetails(params) {
    return this.apiService.postFile('rest/account/', params);
  }
}
