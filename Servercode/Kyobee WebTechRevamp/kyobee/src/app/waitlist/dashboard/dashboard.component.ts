import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  constructor() { }

  dashboardIlluImageSrc="../../../assets/images/dashboard-illu.png";
  notPresentImg="../../../assets/images/not-present.png";
  noteIconImg="../../../assets/images/note-icon.png";
  deleteIconImg="../../../assets/images/delete-icon.png";
  msgIconImg="../../../assets/images/msg-icon.png";

  ngOnInit() {
  }

}
