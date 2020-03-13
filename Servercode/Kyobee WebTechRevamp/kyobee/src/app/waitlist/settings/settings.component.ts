import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { User } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  private checkboEexpanded = false;

  user: User;
  langList = ['Hindi', 'English', 'Gujarati'];
  constructor(private authService: AuthService) {
    this.user = this.authService.getUser();
    console.log("user obj " + JSON.stringify(this.user));
  }

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

  updateNotifyFirst() {
    alert(this.user.notifyFirst);
  }
}
