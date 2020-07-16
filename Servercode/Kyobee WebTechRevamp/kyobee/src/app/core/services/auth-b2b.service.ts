import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';
import { OrganizationDTO } from 'src/app/core/models/organization.model';
import { PlanTermDTO } from 'src/app/core/models/plan-term.model';
import { InvoiceDTO } from 'src/app/core/models/invoice.model';

@Injectable({
  providedIn: 'root'
})
export class AuthB2BService {
  constructor(private apiService: ApiService) {}

  organization: OrganizationDTO = new OrganizationDTO();
  invoice: InvoiceDTO = new InvoiceDTO();
  planTermList: Array<PlanTermDTO>;
  planSummary = {
    waitlist: 0,
    textmarketing: 0
  };
  total;
  orgSubscriptionId;

  setOrganizationData(data) {
    this.organization = data;
  }
  setPlanSummaryDetails(termList, summary, total) {
    this.planTermList = termList;
    this.planSummary = summary;
    this.total = total;
    console.log('subtotal' + this.total);
  }
  setOrgSubscriptionId(Id) {
    this.orgSubscriptionId = Id;
  }
  setInvoiceData(data) {
    this.invoice = data;
  }
}
