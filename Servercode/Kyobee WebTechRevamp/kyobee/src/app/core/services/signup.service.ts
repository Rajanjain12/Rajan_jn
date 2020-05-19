import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SignUpService {
  constructor(private apiService: ApiService) {}

  fetchOrganizationType() {
    return this.apiService.getParams('rest/user/signup/organizationType');
  }
  registerBusiness(params) {
    return this.apiService.post('rest/user/signup/business', params);
  }
}
