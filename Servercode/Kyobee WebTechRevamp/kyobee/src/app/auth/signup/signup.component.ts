import { Component, OnInit } from '@angular/core';
import { SignupDTO } from 'src/app/core/models/signup.model';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {
  password;
  confirmPassword;
  user: SignupDTO = new SignupDTO();
  alertError: { type: string; msg: string; display: Boolean } = { type: '', msg: '', display: false };

  constructor() {}

  ngOnInit() {}
}
