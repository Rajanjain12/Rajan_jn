import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class OrganizationService {
  constructor(private apiService: ApiService) {}

  fetchOrganizationMetrics(orgId: HttpParams) {
    return this.apiService.getParams('rest/waitlist/organizationMetrics', orgId);
  }

  saveOrganizationMetrics(params) {
    return this.apiService.putParams('rest/waitlist/orgSetting', params);
  }

  fetchAllPrefAndLanguageMap() {
    return this.apiService.get('rest/waitlist/setting');
  }


  fetchSmsContent(params) {
    return this.apiService.post('rest/waitlist/organizationTemplate/smsContent', params);
  }
}
