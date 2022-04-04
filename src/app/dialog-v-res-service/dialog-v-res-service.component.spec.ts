import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogVResServiceComponent } from './dialog-v-res-service.component';

describe('DialogVResServiceComponent', () => {
  let component: DialogVResServiceComponent;
  let fixture: ComponentFixture<DialogVResServiceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogVResServiceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogVResServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
