import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSerComponent } from './dialog-ser.component';

describe('DialogSerComponent', () => {
  let component: DialogSerComponent;
  let fixture: ComponentFixture<DialogSerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogSerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
