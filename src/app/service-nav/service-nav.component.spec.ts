import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceNavComponent } from './service-nav.component';

describe('ServiceNavComponent', () => {
  let component: ServiceNavComponent;
  let fixture: ComponentFixture<ServiceNavComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ServiceNavComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ServiceNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
