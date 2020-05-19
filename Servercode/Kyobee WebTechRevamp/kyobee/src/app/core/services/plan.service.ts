import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PlanService {
  constructor(private apiService: ApiService) {}

  fetchPlanDetails(country: HttpParams) {
    return this.apiService.getParams('rest/user/plan/planDetails', country);
  }
  savePlanDetails(params) {
    return this.apiService.postParams('rest/user/plan/planDetails', params);
  }
}
