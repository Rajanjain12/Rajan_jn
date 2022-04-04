import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiamobComponent } from './diamob.component';

describe('DiamobComponent', () => {
  let component: DiamobComponent;
  let fixture: ComponentFixture<DiamobComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiamobComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiamobComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
