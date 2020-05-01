import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SignupB2bComponent } from './signup-b2b.component';

describe('SignupB2bComponent', () => {
  let component: SignupB2bComponent;
  let fixture: ComponentFixture<SignupB2bComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SignupB2bComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SignupB2bComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
