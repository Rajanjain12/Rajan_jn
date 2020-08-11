import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AccountPlanService {
  constructor(private apiService: ApiService) {}

  fetchInvoiceDetails(orgId: HttpParams) {
    return this.apiService.getParams('rest/account/plan/invoice', orgId);
  }
}
