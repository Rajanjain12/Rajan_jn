import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-select-plan',
  templateUrl: './select-plan.component.html',
  styleUrls: ['./select-plan.component.scss']
})
export class SelectPlanComponent implements OnInit {
  @Input('step') step: number;
  @Output('stepChange') stepChange = new EventEmitter<number>();

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

  constructor() {}

  ngOnInit() {}

  onSelectPlan(invalid) {
    if (invalid) {
      return;
    }

    this.step = 3;
    this.stepChange.emit(this.step);
  }
}
