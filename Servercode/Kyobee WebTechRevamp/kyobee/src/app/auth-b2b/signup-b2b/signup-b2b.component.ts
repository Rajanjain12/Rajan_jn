import { Component, OnInit } from '@angular/core';
import { DesignService } from './../../core/services/design.service';

@Component({
  selector: 'app-signup-b2b',
  templateUrl: './signup-b2b.component.html',
  styleUrls: ['./signup-b2b.component.scss']
})
export class SignupB2bComponent implements OnInit {
  alertError: { type: string; msg: string; display: boolean } = { type: '', msg: '', display: false };
  step: number = 0;

  constructor(private designService: DesignService) {
    this.designService.setHeaderStyle('signup');
  }

  ngOnInit() {}
}
