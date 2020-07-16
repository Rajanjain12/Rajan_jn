import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  constructor(private apiService: ApiService) {}

  saveOrgCardDetails(params) {
    return this.apiService.post('rest/user/payment/cardDetails', params);
  }

  createTransaction(params) {
    return this.apiService.post('rest/user/payment/createTransaction', params);
  }

  generateInvoice(params) {
    return this.apiService.post('rest/user/payment/generateInvoice', params);
  }

  calculateDiscount(params) {
    return this.apiService.post('rest/user/payment/discount', params);
  }
}
