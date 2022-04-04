import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogIntComponent } from './dialog-int.component';

describe('DialogIntComponent', () => {
  let component: DialogIntComponent;
  let fixture: ComponentFixture<DialogIntComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogIntComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogIntComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
