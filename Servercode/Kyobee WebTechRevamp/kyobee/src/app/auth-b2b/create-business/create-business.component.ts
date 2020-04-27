import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-create-business',
  templateUrl: './create-business.component.html',
  styleUrls: ['./create-business.component.scss']
})
export class CreateBusinessComponent implements OnInit {
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

  constructor() {}

  ngOnInit() {}

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
