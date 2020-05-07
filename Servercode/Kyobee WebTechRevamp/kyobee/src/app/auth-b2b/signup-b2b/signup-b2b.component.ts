import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-signup-b2b',
  templateUrl: './signup-b2b.component.html',
  styleUrls: ['./signup-b2b.component.scss']
})
export class SignupB2bComponent implements OnInit {
  alertError: { type: string; msg: string; display: boolean } = { type: '', msg: '', display: false };
  step: number = 3;

  constructor() {}

  ngOnInit() {}
}
