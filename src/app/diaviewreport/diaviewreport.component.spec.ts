import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiaviewreportComponent } from './diaviewreport.component';

describe('DiaviewreportComponent', () => {
  let component: DiaviewreportComponent;
  let fixture: ComponentFixture<DiaviewreportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiaviewreportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiaviewreportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
