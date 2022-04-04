import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiaforinterestedComponent } from './diaforinterested.component';

describe('DiaforinterestedComponent', () => {
  let component: DiaforinterestedComponent;
  let fixture: ComponentFixture<DiaforinterestedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiaforinterestedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiaforinterestedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
