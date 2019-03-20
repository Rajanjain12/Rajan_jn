import { Component, OnInit } from '@angular/core';
import { imgLinks } from '../../app-routing.module';

@Component({
  selector: 'app-forgotpwd',
  templateUrl: './forgotpwd.component.html',
  styleUrls: ['./forgotpwd.component.scss']
})
export class ForgotpwdComponent implements OnInit {

  constructor() { }

  public forgotPwdPageImageSrc: string = imgLinks.forgotPwdPageImageSrc;

  ngOnInit() {
  }

}
