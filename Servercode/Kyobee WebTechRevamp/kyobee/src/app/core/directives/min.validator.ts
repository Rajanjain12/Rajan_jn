import { Validator, NG_VALIDATORS, AbstractControl, ValidatorFn } from '@angular/forms';
import { Directive, Input, SimpleChanges } from '@angular/core';
/* 
const min = (min: number): ValidatorFn => {
  return (control: AbstractControl): { [key: string]: any } => {
    let v: number = +control.value;
    return v >= +min ? null : { actualValue: v, requiredValue: +min, min: true };
  };
}; */

@Directive({
  selector: '[minValidator]',
 /*  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: MinValidator,
      multi: true
    }
  ] */
})
export class MinValidator  {
 /*  @Input() min: number; */

  private validator: ValidatorFn;
  private onChange: () => void;

  /* ngOnInit() {
    this.validator = min(this.min);
  }

  ngOnChanges(changes: SimpleChanges) {
    for (let key in changes) {
      if (key === 'min') {
        this.validator = min(changes[key].currentValue);
        if (this.onChange) this.onChange();
      }
    }
  }

  validate(c: AbstractControl): { [key: string]: any } {
    return this.validator(c);
  }

  registerOnValidatorChange(fn: () => void): void {
    this.onChange = fn;
  } */
}
