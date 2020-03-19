import { Validator, NG_VALIDATORS, AbstractControl, ValidatorFn, FormControl } from '@angular/forms';
import { Directive, Input, SimpleChanges } from '@angular/core';

@Directive({
  selector: '[appCustomMin]',
  providers: [{ provide: NG_VALIDATORS, useExisting: MinValidator, multi: true }]
})
export class MinValidator implements Validator {
  @Input() appCustomMin: number;

  validate(c: FormControl): { [key: string]: any } {
    let v = c.value;
    return v < this.appCustomMin ? { appCustomMin: true } : null;
  }
}
