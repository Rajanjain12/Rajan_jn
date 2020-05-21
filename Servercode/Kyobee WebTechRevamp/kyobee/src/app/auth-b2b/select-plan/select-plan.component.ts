import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { PlanService } from 'src/app/core/services/plan.service';
import { HttpParams } from '@angular/common/http';
import { PlanDetailsDTO } from 'src/app/core/models/plan-details.model';
import { PlanDTO } from 'src/app/core/models/plan.model';
import { PlanTermDTO } from 'src/app/core/models/plan-term.model';
import { PlanFeatureDTO } from 'src/app/core/models/plan-feature.model';
import { AuthB2BService } from 'src/app/core/services/auth-b2b.service';

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

  organization;
  isFree = false;

  constructor(private planService: PlanService, public authb2bService: AuthB2BService) {}

  ngOnInit() {
    this.fetchPlanDetails();
  }

  //Purpose:For saving free plan details
  freePlan() {
    this.isFree = true;
    this.savePlanDetails('');
  }

  //Purpose : for saving plan details
  savePlanDetails(invalid) {
    if (invalid) {
      return;
    }
    //If plan is not selected
    else if (this.planSummary.textmarketing === 0 && this.planSummary.waitlist === 0 && this.isFree === false) {
      alert('please select plan');
      return;
    }
    //Save Payment API call
    const params = new HttpParams()
      .set('orgId', '821')
      .set('customerId', '8')
      .set(
        'planFeatureChargeIds',
        this.isFree === true ? [].toString() : [this.planSummary.textmarketing, this.planSummary.waitlist].toString()
      );
    console.log('params:' + params);

    this.planService.savePlanDetails(params).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.orgSubscriptionId = res.serviceResult;
        this.step = 3;
        this.stepChange.emit(this.step);
      } else {
        alert(res.message);
      }
    });
  }

  //Purpose :For fetching plan details acc to country
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

  //Purpose : For fetching details of selected plan term
  selectedPlanTermDetails() {
    this.selectedTermDetails = this.planTermList.find(x => x.termName === this.selectedPlanTerm);
    this.planFeatureList = this.selectedTermDetails.featureList;
  }

  //Purpose : Get selected plan on check/uncheck event of radio button.
  selectPlan(plan, featureName) {
    this.displayPlanSummary = true;
    plan.checked = !plan.checked; // for checking selected plan
    this.total[featureName.toLowerCase().replace(' ', '')] = plan.featureCharge;

    //for reseting the previously selected plan details if radio button is unchecked
    if (plan.checked === false) {
      this.planSummary[featureName.toLowerCase().replace(' ', '')] = 0;
      this.total[featureName.toLowerCase().replace(' ', '')] = 0;
      if (this.planSummary.waitlist === 0 && this.planSummary.textmarketing === 0) {
        this.displayPlanSummary = false;
      }
    }
    //for filtering feature
    this.selectedFeatureDetails = this.planFeatureList.find(x => x.featureName === featureName);
    //For unchecking plan other than selected one
    this.selectedFeatureDetails.featureChargeDetails.forEach(obj => {
      if (obj.planFeatureChargeId != plan.planFeatureChargeId) {
        obj.checked = false;
      }
    });
    console.log('plan:' + JSON.stringify(plan));
    console.log('plan summary:' + JSON.stringify(this.planSummary));

    this.subTotal = this.total.waitlist + this.total.textmarketing;
  }

  //Purpose : For reseting previous plan details
  clearPreviousPlanTermDetails() {
    this.displayPlanSummary = false;
    this.planSummary = {
      waitlist: 0,
      textmarketing: 0
    };
  }
}
