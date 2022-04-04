import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashBarComponent } from './dash-bar.component';

describe('DashBarComponent', () => {
  let component: DashBarComponent;
  let fixture: ComponentFixture<DashBarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashBarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
