import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import * as braintree from 'braintree-web';
import { HttpParams } from '@angular/common/http';
import { AuthB2BService } from 'src/app/core/services/auth-b2b.service';
import { PaymentService } from 'src/app/core/services/payment.service';
import { OrgCardDetailsDTO } from 'src/app/core/models/orgcard-details.model';
import { OrgPaymentDTO } from 'src/app/core/models/org-payment.model';
import { PlanFeatureDTO } from 'src/app/core/models/plan-feature.model';
import { DiscountDTO } from 'src/app/core/models/discount.model';

declare var $: any;

@Component({
  selector: 'app-all-set',
  templateUrl: './all-set.component.html',
  styleUrls: ['./all-set.component.scss']
})
export class AllSetComponent implements OnInit {
  @Input('step') step: number;
  @Output('stepChange') stepChange = new EventEmitter<number>();

  fName;
  lName;
  orgCardDetailsId: number;
  orgCardDetails: OrgCardDetailsDTO = new OrgCardDetailsDTO();
  orgPayment: OrgPaymentDTO = new OrgPaymentDTO();
  hostedFieldsInstance: braintree.HostedFields;
  planFeatureList: Array<PlanFeatureDTO>;
  planSummary = {
    waitlist: 26,
    textmarketing: 24
  };
  subTotal = 50;
  promoCode;
  totalAmount;

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

  discount : DiscountDTO = new DiscountDTO();

  constructor(public authb2bService: AuthB2BService, private paymentService: PaymentService) {}

  ngOnInit() {
    this.createBraintreeUI();
    this.totalAmount = this.authb2bService.total;
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

        this.orgPayment.paymentNonce = payload.nonce;
        this.orgCardDetails.cardName = this.fName + ' ' + this.lName;
        this.orgCardDetails.cardNo = payload.details.lastFour;
        this.orgCardDetails.cardType = payload.details.cardType;
        this.orgCardDetails.vaultID = '';

        const promise = this.saveOrgCardDetails();
        promise.then(value => {
          this.createTransaction();
        });
      })
      .catch(error => {
        console.log(error);
        alert('please enter proper value for feilds');
        return;
      });
  }

  // Purpose: For saving credit/debit card details
  saveOrgCardDetails() {
    var promise = (promise = new Promise((resolve, reject) => {
      this.orgCardDetails.customerId = this.authb2bService.organization.customerId;
      this.orgCardDetails.orgId = this.authb2bService.organization.orgId;
      console.log('organization card details:' + JSON.stringify(this.orgCardDetails));

      this.paymentService.saveOrgCardDetails(this.orgCardDetails).subscribe((res: any) => {
        if (res.success === 1) {
          console.log('response of orgcardDetails:' + JSON.stringify(res.serviceResult));
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

  // Purpose: creating transaction for payment in braintree
  createTransaction() {
    this.orgPayment.orgID = this.authb2bService.organization.orgId;
    this.orgPayment.organizationSubscriptionID = this.authb2bService.orgSubscriptionId;
    this.orgPayment.invoiceID = 0; // have to remove this feild later from both frontend and backend
    this.orgPayment.organizationCardDetailID = this.orgCardDetailsId;
    this.orgPayment.amount = this.totalAmount;

    console.log('Org Payment details:' + JSON.stringify(this.orgPayment));
    this.paymentService.createTransaction(this.orgPayment).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response of create transaction:' + JSON.stringify(res.serviceResult));
        $('#thankYouModal').modal('show');
      } else {
        alert(res.message);
      }
    });
  }

  // Purpose : For closing thank-you popup
  hidePopUp() {
    $('#thankYouModal').modal('hide');
  }

  calculateDiscount() {
    const params = new HttpParams().set('amount', this.authb2bService.total).set('promoCode', this.promoCode);
    this.discount.amount = this.authb2bService.total;
    this.discount.promoCode = this.promoCode;
    this.discount.invoiceDTO = this.authb2bService.invoice;

    this.paymentService.calculateDiscount(this.discount).subscribe((res: any) => {
      if (res.success === 1) {
        this.totalAmount = res.serviceResult;
        this.authb2bService.total = res.serviceResult;
        console.log('response of promocode:' + JSON.stringify(res.serviceResult));
      } else {
        alert(res.message);
      }
    });
  }
}
