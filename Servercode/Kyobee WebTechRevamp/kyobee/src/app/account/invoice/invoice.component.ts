import { Component, OnInit } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { User } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { AccountPlanService } from 'src/app/core/services/account-plan.service';
import { InvoiceDetailsDTO } from 'src/app/core/models/invoice-details.model';

@Component({
  selector: 'app-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.scss']
})
export class InvoiceComponent implements OnInit {
  user: User = new User(); // for storing user details
  invoiceDetailsList: Array<InvoiceDetailsDTO>;

  constructor(private authService: AuthService, private accountPlanService: AccountPlanService) {}

  ngOnInit() {
    this.user = this.authService.getUser();
    this.fetchInvoiceDetails();
  }

  // Purpose : for fetching invoice details
  fetchInvoiceDetails() {
    const params = new HttpParams().set('orgId', this.user.organizationID.toString());

    this.accountPlanService.fetchInvoiceDetails(params).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.invoiceDetailsList = res.serviceResult;
      } else {
        alert(res.message);
      }
    });
  }

  downloadFile(url) {
    console.log(url);
    window.location.href = url;
  }
}
