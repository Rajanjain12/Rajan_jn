import { Directive, Input, SimpleChanges } from '@angular/core';
import { NG_VALIDATORS, Validator, ValidatorFn, AbstractControl } from '@angular/forms';

export const max = (max: number): ValidatorFn => {
  return (control: AbstractControl): { [key: string]: any } => {
    let v: number = +control.value;
    console.log(v);
    return v <= +max ? null : { actualValue: v, requiredValue: +max, max: true };
  };
};

@Directive({
  selector: '[maxValidator]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: MaxValidator,
      multi: true
    }
  ]
})
export class MaxValidator implements Validator {
  @Input() max: number;

  private validator: ValidatorFn;
  private onChange: () => void;

  ngOnInit() {
    this.validator = max(this.max);
  }

  ngOnChanges(changes: SimpleChanges) {
    for (let key in changes) {
      if (key === 'max') {
        this.validator = max(changes[key].currentValue);
        if (this.onChange) this.onChange();
      }
    }
  }

  validate(c: AbstractControl): { [key: string]: any } {
    return this.validator(c);
  }

  registerOnValidatorChange(fn: () => void): void {
    this.onChange = fn;
  }
}
