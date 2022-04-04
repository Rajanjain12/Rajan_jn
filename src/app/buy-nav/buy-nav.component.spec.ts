import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyNavComponent } from './buy-nav.component';

describe('BuyNavComponent', () => {
  let component: BuyNavComponent;
  let fixture: ComponentFixture<BuyNavComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuyNavComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuyNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
