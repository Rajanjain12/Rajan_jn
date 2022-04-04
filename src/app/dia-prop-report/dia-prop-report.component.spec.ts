import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiaPropReportComponent } from './dia-prop-report.component';

describe('DiaPropReportComponent', () => {
  let component: DiaPropReportComponent;
  let fixture: ComponentFixture<DiaPropReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiaPropReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiaPropReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
