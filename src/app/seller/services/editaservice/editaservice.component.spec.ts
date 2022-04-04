import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditaserviceComponent } from './editaservice.component';

describe('EditaserviceComponent', () => {
  let component: EditaserviceComponent;
  let fixture: ComponentFixture<EditaserviceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditaserviceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditaserviceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
