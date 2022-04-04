import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialoginComponent } from './dialogin.component';

describe('DialoginComponent', () => {
  let component: DialoginComponent;
  let fixture: ComponentFixture<DialoginComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialoginComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
