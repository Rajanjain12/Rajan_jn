import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

declare var $: any;

@Component({
  selector: 'app-create-business',
  templateUrl: './create-business.component.html',
  styleUrls: ['./create-business.component.scss']
})
export class CreateBusinessComponent implements OnInit {
  @Input('step') step: number;
  @Output('stepChange') stepChange = new EventEmitter<number>();

  // Todo: Make Model of it
  createBusiness: {
    firstName: string;
    lastName: string;
    userName: string;
    email: string;
    password: string;
    confPassword: string;
  } = {
    firstName: '',
    lastName: '',
    userName: '',
    email: '',
    password: '',
    confPassword: ''
  };

  veriCode: number[] = new Array(6);
  incorrectActCode: boolean;

  constructor() {}

  ngOnInit() {}

  onCreateBusiness(invalid) {
    if (invalid) {
      return;
    }

    $('#verificationCodeModal').modal('show');
  }

  onVerificationCode(invalid) {
    if (invalid) {
      return;
    }
    $('#verificationCodeModal').modal('hide');

    this.step = 2;
    this.stepChange.emit(this.step);
  }

  trackByIndex(index: number, obj: any): any {
    return index;
  }

  keytab(myIndex, key, event) {
    if (event === 'keyup' && (key.keyCode === 8 || key.keyCode === 46)) {
      key.target.value = '';
      if (myIndex > 0) {
        myIndex = myIndex - 1;
        const myIndex2 = 'activationCode' + myIndex;
        document.getElementById(myIndex2).focus();
      }
    }
    if (event === 'input' && myIndex < 5 && key.target.value !== '') {
      if (key.data in [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]) {
        myIndex = myIndex + 1;
        const myIndex2 = 'activationCode' + myIndex;
        document.getElementById(myIndex2).focus();
      } else {
        key.target.value = '';
      }
    }
  }

  toggleEye(event) {
    const targetId = event.target.attributes.toggle.value;

    if (document.querySelector(targetId).getAttribute('type') === 'password') {
      document.querySelector(targetId).setAttribute('type', 'text');
      event.target.className = 'grey flaticon-unlocked eye-icon toggle-password font-weight-semibold';
    } else {
      document.querySelector(targetId).setAttribute('type', 'password');
      event.target.className = 'grey flaticon-padlock eye-icon toggle-password font-weight-semibold';
    }
  }
}
