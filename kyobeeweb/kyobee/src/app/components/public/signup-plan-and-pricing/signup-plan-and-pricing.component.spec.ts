import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SignupPlanAndPricingComponent } from './signup-plan-and-pricing.component';

describe('SignupPlanAndPricingComponent', () => {
  let component: SignupPlanAndPricingComponent;
  let fixture: ComponentFixture<SignupPlanAndPricingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SignupPlanAndPricingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SignupPlanAndPricingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
