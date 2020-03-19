import { Directive, Input, SimpleChanges, ElementRef } from '@angular/core';
import { NG_VALIDATORS, Validator, ValidatorFn, AbstractControl, FormControl } from '@angular/forms';

@Directive({
  selector: '[appCustomMax]',
  providers: [{ provide: NG_VALIDATORS, useExisting: MaxValidator, multi: true }]
})
export class MaxValidator implements Validator {
  @Input() appCustomMax: number;

  validate(c: FormControl): { [key: string]: any } {
    let v = c.value;
    return v > this.appCustomMax ? { appCustomMax: true } : null;
  }
}
