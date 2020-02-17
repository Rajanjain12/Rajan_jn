import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GuestDetailUpdateComponent } from './guest-detail-update.component';

describe('GuestDetailUpdateComponent', () => {
  let component: GuestDetailUpdateComponent;
  let fixture: ComponentFixture<GuestDetailUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GuestDetailUpdateComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GuestDetailUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
