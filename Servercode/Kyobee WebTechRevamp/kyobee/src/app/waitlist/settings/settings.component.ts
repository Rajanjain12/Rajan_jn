import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { User } from 'src/app/core/models/user.model';
import { SmsTemplate } from 'src/app/core/models/sms-template.model';
import { OrganizationService } from 'src/app/core/services/organization.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  private checkboEexpanded = false;

  user: User;
  selectedSMSTemplateLangId;
  selectedSMSTemplateText: Array<SmsTemplate>;

  constructor(private authService: AuthService, private orgService: OrganizationService) {
    this.user = this.authService.getUser();
    this.selectedSMSTemplateLangId = this.user.defaultLangId;
    this.changeSelectedTemplate();
    this.fetchAllPrefAndKeyMap();
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
    alert(this.user.defaultLangId);
  }

  changeSelectedTemplate() {
    this.selectedSMSTemplateText = new Array<SmsTemplate>();
    alert(this.selectedSMSTemplateLangId);
    this.user.smsTemplate.map((template) => {
      if ( template.languageID === parseInt(this.selectedSMSTemplateLangId, 10)) {
        this.selectedSMSTemplateText.push(template);
      }
    });
  }
 fetchAllPrefAndKeyMap() {
    this.orgService.fetchAllPrefAndLanguageMap().subscribe((res: any) => {
      if (res.success === 1) {
       console.log( " response " + JSON.stringify(res.serviceResult));
      } else {
        alert(res.message);
      }
    });
 }
}
