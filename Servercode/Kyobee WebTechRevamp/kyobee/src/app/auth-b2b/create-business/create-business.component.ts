import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UserSignupDTO } from 'src/app/core/models/usersignup.model';
import { AuthB2BService } from 'src/app/core/services/auth-b2b.service';
import { UserService } from 'src/app/core/services/user.service';
import { HttpParams } from '@angular/common/http';
import { LoaderService } from 'src/app/core/services/loader.service';

declare var $: any;

@Component({
  selector: 'app-create-business',
  templateUrl: './create-business.component.html',
  styleUrls: ['./create-business.component.scss']
})
export class CreateBusinessComponent implements OnInit {
  @Input('step') step: number;
  @Output('stepChange') stepChange = new EventEmitter<number>();

  orgUser: UserSignupDTO = new UserSignupDTO();
  userId;

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

  constructor(
    private authb2bService: AuthB2BService,
    private userService: UserService,
    public loaderService: LoaderService
  ) {}

  ngOnInit() {}

  //Purpose : For Saving User
  saveUser(invalid) {
    if (invalid) {
      return;
    }
    // getting customer Id and organization Id from recently saved organization
    this.orgUser.customerId = this.authb2bService.organization.customerId;
    this.orgUser.orgId = this.authb2bService.organization.orgId;
    console.log('user:' + JSON.stringify(this.orgUser));

    //saving user
    this.userService.saveUser(this.orgUser).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.userId = res.serviceResult;
        $('#verificationCodeModal').modal('show');
      } else {
        alert(res.message);
      }
    });
  }

  activateUser(invalid) {
    if (invalid) {
      return;
    }
    console.log('code:' + this.veriCode.toString().replace(/,/g, ''));
    console.log(' activate user id:' + this.userId);
    const params = new HttpParams()
      .set('activationCode', this.veriCode.toString().replace(/,/g, ''))
      .set('userId', this.userId);

    this.userService.activateUser(params).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        $('#verificationCodeModal').modal('hide');
        this.step = 2;
        this.stepChange.emit(this.step);
      } else {
        alert(res.message);
      }
    });
  }

  resendCode() {
    this.loaderService.disable = true;
    const params = new HttpParams().set('userId', this.userId);
    console.log(' resend user id:' + this.userId);
    this.userService.resendCode(params).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
      } else {
        alert(res.message);
      }
    });
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
