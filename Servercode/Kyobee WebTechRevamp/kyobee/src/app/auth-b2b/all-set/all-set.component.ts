import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import * as braintree from 'braintree-web';
import { HttpParams } from '@angular/common/http';
import { AuthB2BService } from 'src/app/core/services/auth-b2b.service';
import { PaymentService } from 'src/app/core/services/payment.service';
import { OrgCardDetailsDTO } from 'src/app/core/models/orgcard-details.model';
import { OrgPaymentDTO } from 'src/app/core/models/org-payment.model';

@Component({
  selector: 'app-all-set',
  templateUrl: './all-set.component.html',
  styleUrls: ['./all-set.component.scss']
})
export class AllSetComponent implements OnInit {
  @Input('step') step: number;
  @Output('stepChange') stepChange = new EventEmitter<number>();

  orgCardDetailsId;
  orgCardDetails: OrgCardDetailsDTO = new OrgCardDetailsDTO();
  orgPayment: OrgPaymentDTO = new OrgPaymentDTO();
  hostedFieldsInstance: braintree.HostedFields;

  payment: {
    firstName: string;
    lastName: string;
    cardNumber: string;
    expiryDate: string;
    cvv: string;
    promoCode: string;
  } = {
    firstName: 'Ria',
    lastName: 'Sahesnani',
    cardNumber: '4000111111111115',
    expiryDate: '05/2021',
    cvv: '100',
    promoCode: '1111'
  };

  selectPlan: {
    planType: string;
    restaurantAddress: { title: string; address: string; email: string };
    planSummary: {
      waitlist: number;
      textMarketing: number;
    };
  } = {
    planType: 'Month',
    restaurantAddress: {
      title: 'Alexveri',
      address: '#128/69, 2nd street, Welcraft street Sharington Post, Berlin - 56005 Germany',
      email: 'alexveri@gmail.com'
    },
    planSummary: {
      waitlist: 1,
      textMarketing: 1
    }
  };

  planList: {
    waitlist: Array<{
      id: number;
      name: string;
      price: number;
      planType: string;
    }>;
    textMarketing: Array<{ id: number; name: string; price: number; planType: string }>;
  } = {
    waitlist: [
      { id: 1, name: 'Silver', price: 26, planType: 'Month' },
      { id: 2, name: 'Gold', price: 52, planType: 'Month' },
      { id: 3, name: 'Silver', price: 200, planType: 'Annum' },
      { id: 4, name: 'Gold', price: 440, planType: 'Annum' }
    ],
    textMarketing: [
      { id: 1, name: 'Silver', price: 10, planType: 'Month' },
      { id: 2, name: 'Gold', price: 20, planType: 'Month' },
      { id: 3, name: 'Silver', price: 100, planType: 'Annum' },
      { id: 4, name: 'Gold', price: 200, planType: 'Annum' }
    ]
  };

  constructor(private authb2bService: AuthB2BService, private paymentService: PaymentService) {}

  ngOnInit() {
    this.createBraintreeUI();
  }

  createBraintreeUI() {
    braintree.client
      .create({
        authorization: 'sandbox_24jnfdmj_k5dtfbdcdw6brmhv'
      })
      .then(clientInstance => {
        braintree.hostedFields
          .create({
            client: clientInstance,
            styles: {
              input: {
                color: '#333',
                display: 'block',
                background: 'none',
                padding: ' 0.125rem 0.125rem 0.0625rem',
                'font-size': '1rem',
                'border-width': '0',
                'border-color': 'transparent',
                'line-height': '1.9',
                width: '100%',
                '-webkit-transition': 'all 0.28s ease',
                transition: 'all 0.28s ease',
                'box-shadow': 'none'
              },

              '.number': {
                'font-family': '"Source Sans Pro", sans-serif'
              },

              '.valid': {
                color: 'green'
              }
            },

            // The hosted fields that we will be using
            // NOTE : cardholder's name field is not available in the field options
            // and a separate input field has to be used incase you need it
            fields: {
              number: {
                selector: '#cardNumber'
              },
              cvv: {
                selector: '#cvv'
              },
              expirationDate: {
                selector: '#expiryDate'
              }
            }
          })
          .then(hostedFieldsInstance => {
            this.hostedFieldsInstance = hostedFieldsInstance;
          });
      });
  }

  onPayment(invalid) {
    if (invalid) {
      return;
    }

    this.hostedFieldsInstance
      .tokenize()
      .then(payload => {
        console.log('tokenize');
        console.log(payload);
        console.log(payload.nonce);
      })
      .catch(error => {
        console.log(error);
      });

    /* const promise = this.saveOrgCardDetails();
    promise.then(value => {
      console.log("transaction");
      this.createTransaction();
    });*/
  }

  //Purpose: For saving credit/debit card details
  saveOrgCardDetails() {
    var promise = (promise = new Promise((resolve, reject) => {
      const params = new HttpParams()
        .set('orgId', this.authb2bService.organization.orgId.toString())
        .set('customerId', this.authb2bService.organization.customerId.toString())
        .set('orgCardDetailsDTO', this.orgCardDetails.toString());

      this.paymentService.saveOrgCardDetails(params).subscribe((res: any) => {
        if (res.success === 1) {
          console.log('response:' + JSON.stringify(res.serviceResult));
          this.orgCardDetailsId = res.serviceResult;
          resolve();
        } else {
          alert(res.message);
          reject();
        }
      });
    }));
    return promise;
  }

  //Purpose: creating transaction for payment in braintree
  createTransaction() {
    this.paymentService.saveOrgCardDetails(this.orgPayment).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
      } else {
        alert(res.message);
      }
    });
  }
}
