import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResetpwdThanksComponent } from './resetpwd-thanks.component';

describe('ResetpwdThanksComponent', () => {
  let component: ResetpwdThanksComponent;
  let fixture: ComponentFixture<ResetpwdThanksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResetpwdThanksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetpwdThanksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
