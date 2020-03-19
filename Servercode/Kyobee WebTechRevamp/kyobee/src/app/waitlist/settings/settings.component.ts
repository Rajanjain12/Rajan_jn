import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { User } from 'src/app/core/models/user.model';
import { SmsTemplate } from 'src/app/core/models/sms-template.model';
import { Preference } from 'src/app/core/models/preference.model';
import { LanguageKeyMappingDTO } from 'src/app/core/models/language-keymap.model';
import { OrgSettingDTO } from 'src/app/core/models/organization-setting.model';
import { OrganizationService } from 'src/app/core/services/organization.service';
import { HttpParams } from '@angular/common/http';
import { typeWithParameters } from '@angular/compiler/src/render3/util';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  private checkboEexpanded = false;

  user: User = new User();
  selectedSMSTemplateLangId;
  selectedSMSTemplateText: Array<SmsTemplate>;
  defaultPrefAndKeyMap;
  seatingPrefList: Array<Preference> = new Array();
  marketingPrefList: Array<Preference> = new Array();
  defaultLanguageList: any = [];
  languageList: Array<LanguageKeyMappingDTO> = new Array();
  orgLanguageList: any = [];
  selectedSeatingPref: Array<Preference> = new Array();
  selectedMarketingPref: Array<Preference> = new Array();
  SMSTemplateText: Array<SmsTemplate> = new Array();
  orgSettingDTO: OrgSettingDTO;
  successMsg = null;

  constructor(private authService: AuthService, private orgService: OrganizationService) {
    this.user = this.authService.getUser();
    this.selectedSMSTemplateLangId = this.user.defaultLangId;
    this.changeSelectedTemplate();
  }

  ngOnInit() {
    this.defaultLanguageList = ['English', 'French', 'Chinese', 'Chinese (Traditional)', 'Korean'];
    this.user = this.authService.getUser();
    this.orgLanguageList = this.user.languagePref;
    this.fetchAllPrefAndKeyMap();
  }

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

  changeSelectedTemplate() {
    this.selectedSMSTemplateText = new Array<SmsTemplate>();
    //alert(this.selectedSMSTemplateLangId);
    this.user.smsTemplate.map(template => {
      if (template.languageID === parseInt(this.selectedSMSTemplateLangId, 10)) {
        this.selectedSMSTemplateText.push(template);
      }
    });
  }

  //Purpose : for fetching org pref and language key map
  fetchAllPrefAndKeyMap() {
    this.orgService.fetchAllPrefAndLanguageMap().subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.seatingPrefList = res.serviceResult.seatingPreference;
        this.marketingPrefList = res.serviceResult.marketingPreference;

        this.setDefaultPrefAndAddLang(res);
      } else {
        alert(res.message);
      }
    });
  }

  //Purpose : for setting default Preference and additional language
  setDefaultPrefAndAddLang(res) {
    // for default selection of marketing preference added by organization
    this.user.marketingPref.forEach(obj => {
      var marketingPrefExists = this.marketingPrefList.find(({ prefValue }) => obj.prefValue === prefValue);
      if (marketingPrefExists) {
        marketingPrefExists.selected = true;
      }
    });
    // for default selection of seating preference added by organization
    this.user.seatingpref.forEach(obj => {
      var seatingPrefExists = this.seatingPrefList.find(({ prefValue }) => obj.prefValue === prefValue);
      if (seatingPrefExists) {
        seatingPrefExists.selected = true;
      }
    });

    //for displaying additional language list
    this.defaultLanguageList.forEach(obj => {
      var langExists = res.serviceResult.languageList.find(x => x.langName === obj);
      if (langExists) {
        this.languageList.push(langExists);
      }
    });

    //to checked language from list that is already added by organization
    this.orgLanguageList.forEach(obj => {
      var languageExists = this.languageList.find(x => x.langName === obj.langName);
      if (languageExists) {
        languageExists.checked = true;
      }
    });
  }

  getSelectedLanguage(lang, selected) {
    console.log('selected lanfg:' + selected + '' + JSON.stringify(lang));
    if (selected) {
      this.addSelectedLanguage(lang);
    } else {
      this.removeLanguage(lang);
    }
  }

  addSelectedLanguage(lang) {
    const params = new HttpParams().set('langId', lang.langId).set('orgId', this.user.organizationID.toString());

    this.orgService.addLanguage(params).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('Language Added = ' + JSON.stringify(res));
        this.user.languagePref.push(lang);
        //this.user.smsTemplate.push(res.serviceResult);

        res.serviceResult.forEach(obj => {
          this.user.smsTemplate.push(obj);
        });
        this.authService.setSessionData(this.user);
        console.log(this.user);
      } else {
        alert(res.message);
      }
    });
  }

  removeLanguage(lang) {
    const params = new HttpParams().set('orgId', this.user.organizationID.toString()).set('langId', lang.langId);

    this.orgService.deleteLanguage(params).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('Language deleted = ' + JSON.stringify(res));
        this.user.languagePref = this.user.languagePref.filter(opt => opt.langId !== lang.langId);
        this.user.smsTemplate = this.user.smsTemplate.filter(opt => opt.languageID !== lang.langId);
        this.authService.setSessionData(this.user);
        console.log(this.user);
      } else {
        alert(res.message);
      }
    });
  }

  saveOrgSetting() {
    this.user.seatingpref = [];
    this.user.marketingPref = [];

    this.selectedSeatingPref = this.seatingPrefList.filter(opt => opt.selected);
    this.selectedSeatingPref.forEach(obj => {
      this.user.seatingpref.push(obj);
    });

    this.selectedMarketingPref = this.marketingPrefList.filter(opt => opt.selected);
    console.log('this.selectedMarketingPref:' + JSON.stringify(this.selectedMarketingPref));
    this.selectedMarketingPref.forEach(obj => {
      this.user.marketingPref.push(obj);
    });

    console.log('notify first' + JSON.stringify(this.user.notifyFirst));

    this.orgSettingDTO = new OrgSettingDTO();
    this.orgSettingDTO.seatingPreference = this.user.seatingpref;
    this.orgSettingDTO.marketingPreference = this.user.marketingPref;
    this.orgSettingDTO.defaultLanguage = this.user.defaultLangId;
    this.orgSettingDTO.notifyFirst = this.user.notifyFirst;
    this.orgSettingDTO.smsTemplateDTO = this.user.smsTemplate;
    this.orgSettingDTO.orgId = this.user.organizationID;

    this.orgService.saveOrganizationSetting(this.orgSettingDTO).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('org response:' + res.serviceResult);
        this.authService.setSessionData(this.user);
        console.log(this.user);
        this.successMsg = res.serviceResult;
      } else {
        alert(res.message);
      }
    });
  }
  dosomething(event, level) {
    console.log('event:' + JSON.stringify(event));

    this.user.smsTemplate.map(template => {
      if (template.languageID === parseInt(this.selectedSMSTemplateLangId, 10) && template.level === level) {
        template.templateText = event;
        this.SMSTemplateText.push(template);
      }
    });

    console.log('user template:' + JSON.stringify(this.user.smsTemplate));
  }
}
