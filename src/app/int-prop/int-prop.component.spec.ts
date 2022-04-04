import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IntPropComponent } from './int-prop.component';

describe('IntPropComponent', () => {
  let component: IntPropComponent;
  let fixture: ComponentFixture<IntPropComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IntPropComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IntPropComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
