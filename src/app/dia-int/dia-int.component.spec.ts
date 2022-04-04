import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiaIntComponent } from './dia-int.component';

describe('DiaIntComponent', () => {
  let component: DiaIntComponent;
  let fixture: ComponentFixture<DiaIntComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiaIntComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiaIntComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
