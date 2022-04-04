import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiamsgComponent } from './diamsg.component';

describe('DiamsgComponent', () => {
  let component: DiamsgComponent;
  let fixture: ComponentFixture<DiamsgComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiamsgComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiamsgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
