import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { PlanService } from 'src/app/core/services/plan.service';
import { HttpParams } from '@angular/common/http';
import { PlanDetailsDTO } from 'src/app/core/models/plan-details.model';
import { PlanDTO } from 'src/app/core/models/plan.model';
import { PlanTermDTO } from 'src/app/core/models/plan-term.model';
import { PlanFeatureDTO } from 'src/app/core/models/plan-feature.model';
import { AuthB2BService } from 'src/app/core/services/auth-b2b.service';
import { PaymentService } from 'src/app/core/services/payment.service';
import { LoaderService } from 'src/app/core/services/loader.service';
import { OrganizationDTO } from 'src/app/core/models/organization.model';
import { InvoiceDTO } from 'src/app/core/models/invoice.model';

declare var $: any;

@Component({
  selector: 'app-select-plan',
  templateUrl: './select-plan.component.html',
  styleUrls: ['./select-plan.component.scss']
})
export class SelectPlanComponent implements OnInit {
  @Input('step') step: number;
  @Output('stepChange') stepChange = new EventEmitter<number>();

  country = 'United States';
  planDetailsList: Array<PlanDetailsDTO>;
  planList: Array<PlanDTO>;
  planTermList: Array<PlanTermDTO>;
  planFeatureList: Array<PlanFeatureDTO>;
  selectedPlanTerm = 'Monthly';
  selectedTermDetails: PlanTermDTO;
  planSummary = {
    waitlist: 0,
    textmarketing: 0
  };

  total = {
    waitlist: 0,
    textmarketing: 0
  };
  subTotal = 0;
  selectedFeatureDetails: PlanFeatureDTO;
  displayPlanSummary = false;
  orgSubscriptionId;
  invoice: InvoiceDTO = new InvoiceDTO();

  organization: OrganizationDTO = new OrganizationDTO();
  isFree = false;
  hide = true;
  limit = [75,75];
  text = ['See Details', 'See Details'];

  constructor(
    private planService: PlanService,
    public authb2bService: AuthB2BService,
    private paymentService: PaymentService,
    public loaderService: LoaderService
  ) {}

  ngOnInit() {

    this.fetchPlanDetails();
  }

  // Purpose:For saving free plan details
  freePlan() {
    this.isFree = true;
    this.savePlanDetails('');
    $('#thankYouModal').modal('show');
  }

  // Purpose : for saving plan details
  savePlanDetails(invalid) {
    if (invalid) {
      return;
    }
    // If plan is not selected
    else if (this.planSummary.textmarketing === 0 && this.planSummary.waitlist === 0 && this.isFree === false) {
      alert('please select plan');
      return;
    }
    // Save plan API call
    const params = new HttpParams()
      .set('orgId', this.authb2bService.organization.orgId.toString())
      .set('customerId', this.authb2bService.organization.customerId.toString())
      .set(
        'planFeatureChargeIds',
        this.isFree === true ? [].toString() : [this.planSummary.textmarketing, this.planSummary.waitlist].toString()
      );
    console.log('params:' + params);
    this.authb2bService.setPlanSummaryDetails(this.planTermList, this.planSummary, this.subTotal);

    this.planService.savePlanDetails(params).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.orgSubscriptionId = res.serviceResult;
        this.authb2bService.setOrgSubscriptionId(this.orgSubscriptionId);
        this.authb2bService.setPlanSummaryDetails(this.planFeatureList, this.planSummary, this.subTotal);
        // If paid plan then generate invoice
        if (!(this.isFree === true)) {
          this.generateInvoice();
          this.step = 3;
          this.stepChange.emit(this.step);
        }

      } else {
        alert(res.message);
      }
    });
  }

  // Purpose :For fetching plan details acc to country
  fetchPlanDetails() {
    const params = new HttpParams().set('country', this.country);

    this.planService.fetchPlanDetails(params).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.planDetailsList = res.serviceResult;
        this.planList = res.serviceResult.planList;
        this.planTermList = res.serviceResult.termList;
        this.selectedTermDetails = this.planTermList.find(x => x.termName === this.selectedPlanTerm);
        this.planFeatureList = this.selectedTermDetails.featureList;
      } else {
        alert(res.message);
      }
    });
  }

  // Purpose : For fetching details of selected plan term
  selectedPlanTermDetails() {
    this.selectedTermDetails = this.planTermList.find(x => x.termName === this.selectedPlanTerm);
    this.planFeatureList = this.selectedTermDetails.featureList;
  }

  // Purpose : Get selected plan on check/uncheck event of radio button.
  selectPlan(plan, featureName) {
    this.displayPlanSummary = true;
    plan.checked = !plan.checked; // for checking selected plan
    this.total[featureName.toLowerCase().replace(' ', '')] = plan.featureCharge;

    // For reseting the previously selected plan details if radio button is unchecked
    if (plan.checked === false) {
      this.planSummary[featureName.toLowerCase().replace(' ', '')] = 0;
      this.total[featureName.toLowerCase().replace(' ', '')] = 0;
      if (this.planSummary.waitlist === 0 && this.planSummary.textmarketing === 0) {
        this.displayPlanSummary = false;
      }
    }
    // For filtering feature
    this.selectedFeatureDetails = this.planFeatureList.find(x => x.featureName === featureName);
    // For unchecking plan other than selected one
    this.selectedFeatureDetails.featureChargeDetails.forEach(obj => {
      if (obj.planFeatureChargeId != plan.planFeatureChargeId) {
        obj.checked = false;
      }
    });
    console.log('plan:' + JSON.stringify(plan));
    console.log('plan summary:' + JSON.stringify(this.planSummary));

    this.subTotal = this.total.waitlist + this.total.textmarketing;
  }

  // Purpose : For reseting previous plan details
  clearPreviousPlanTermDetails() {
    this.displayPlanSummary = false;
    /* this.planSummary = {
      waitlist: 0,
      textmarketing: 0
    };*/
  }

  // Purpose : Generate Invoice for selected plan
  generateInvoice() {
    this.loaderService.disable = true;
    console.log('generating invoice');
    console.log('orgDTO' + JSON.stringify(this.authb2bService.organization));
    this.invoice.orgSubscriptionId = this.orgSubscriptionId;
    this.invoice.featureChargeIds =
      this.isFree === true ? [] : [this.planSummary.textmarketing, this.planSummary.waitlist];
    this.invoice.orgDTO = this.authb2bService.organization;
    this.invoice.discount = 0;
    console.log('invocie data:' + JSON.stringify(this.invoice));

    this.paymentService.generateInvoice(this.invoice).subscribe((res: any) => {
      this.authb2bService.setInvoiceData(this.invoice);
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.step = 3;
        this.stepChange.emit(this.step);
      } else {
        alert(res.message);
      }
    });
  }

  // Purpose : For closing thank-you popup
  hidePopUp() {
    $('#thankYouModal').modal('hide');
  }

  //
  toggle(description, name, index) {
    if (this.text[index] === 'See Details') {
      this.text[index]  = 'Hide Details';
      this.limit[index] = description.length;
    } else {
      this.text[index]  = 'See Details';
      this.limit [index]= 75;
    }
  }
}
