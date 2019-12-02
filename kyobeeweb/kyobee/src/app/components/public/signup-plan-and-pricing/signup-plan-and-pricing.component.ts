import { Component, OnInit } from '@angular/core';
import { HttpService } from 'src/app/services/http.service';
import { DataService } from 'src/app/services/data.service';


@Component({
  selector: 'app-signup-plan-and-pricing',
  templateUrl: './signup-plan-and-pricing.component.html',
  styleUrls: ['./signup-plan-and-pricing.component.scss']
})
export class SignupPlanAndPricingComponent implements OnInit {
  public selectedPlan : string ="";
  constructor( private httpService: HttpService, private dataService: DataService) { 

  }


  ngOnInit() {
  }

  onSelectPlan(planName){
    alert(planName);
    this.selectedPlan=planName;
    
    this.httpService.changeViewWithPara("/public/signup/",planName);
  }

}
