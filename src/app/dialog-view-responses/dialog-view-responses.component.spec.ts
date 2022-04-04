import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogViewResponsesComponent } from './dialog-view-responses.component';

describe('DialogViewResponsesComponent', () => {
  let component: DialogViewResponsesComponent;
  let fixture: ComponentFixture<DialogViewResponsesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogViewResponsesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogViewResponsesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
