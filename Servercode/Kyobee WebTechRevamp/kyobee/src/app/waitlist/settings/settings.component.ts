import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  private checkboEexpanded = false;

  langList = ['Hindi', 'English', 'Gujarati'];
  constructor() {}

  ngOnInit() {}

  showCheckboxes() {
    const checkboxes = document.getElementById('checkboxes');

    if (!this.checkboEexpanded) {
      checkboxes.className = 'showDropdown';
      this.checkboEexpanded = true;
    } else {
      checkboxes.className = 'hideDropdown';
      this.checkboEexpanded = false;
    }
  }
}
