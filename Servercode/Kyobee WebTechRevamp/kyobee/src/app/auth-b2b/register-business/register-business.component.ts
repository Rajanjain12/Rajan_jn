import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-register-business',
  templateUrl: './register-business.component.html',
  styleUrls: ['./register-business.component.scss']
})
export class RegisterBusinessComponent implements OnInit {
  countryList: Array<{ code: string; name: string }> = [
    { code: 'us', name: 'United State' },
    { code: 'in', name: 'India' }
  ];
  data: Array<{ id: number; name: string }> = [
    {
      id: 1,
      name: 'The Imperial Palace summertime ln, Culver City, CA,90230'
    },
    {
      id: 2,
      name: 'Lords Restaurant near The Imperial Palace summertime ln, Culver City, CA,90230'
    },
    {
      id: 3,
      name: 'The Imperial Palace summertime ln, Culver City, CA,90230'
    },
    {
      id: 4,
      name: 'Lords Restaurant near The Imperial Palace summertime ln, Culver City, CA,90230'
    },
    {
      id: 5,
      name: 'The Imperial Palace summertime ln, Culver City, CA,90230'
    },
    {
      id: 6,
      name: 'Lords Restaurant near The Imperial Palace summertime ln, Culver City, CA,90230'
    },
    {
      id: 7,
      name: 'The Imperial Palace summertime ln, Culver City, CA,90230'
    },
    {
      id: 8,
      name: 'Lords Restaurant near The Imperial Palace summertime ln, Culver City, CA,90230'
    }
  ];
  addBusinessFlag: boolean = false;
  addBusiness: {
    businessName: string;
    businessPhoneNumber: string;
    streetAddress: string;
    IndustryInfo: string;
    countryCode: string;
    city: string;
    state: string;
    zipcode: string;
    timezone: string;
  } = {
    businessName: '',
    businessPhoneNumber: '',
    streetAddress: '',
    IndustryInfo: '',
    countryCode: 'us',
    city: '',
    state: '',
    zipcode: '',
    timezone: ''
  };

  constructor() { }

  ngOnInit() {
  }

}
