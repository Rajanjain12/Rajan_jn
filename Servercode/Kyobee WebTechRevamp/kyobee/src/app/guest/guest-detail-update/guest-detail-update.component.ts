import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-guest-detail-update',
  templateUrl: './guest-detail-update.component.html',
  styleUrls: ['./guest-detail-update.component.scss']
})
export class GuestDetailUpdateComponent implements OnInit {
  constructor() {}

  ngOnInit() {}

  onFormSubmit(form: NgForm) {
    console.log('TCL: GuestDetailUpdateComponent -> onFormSubmit -> form', form);
  }
}
