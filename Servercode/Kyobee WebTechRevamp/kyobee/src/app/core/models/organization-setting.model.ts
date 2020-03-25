import { Preference } from './preference.model';
import { SmsTemplate } from './sms-template.model';
import { LanguageKeyMappingDTO } from './language-keymap.model';

export class OrgSettingDTO {
  orgId: number;
  seatingPreference: Array<Preference>;
  marketingPreference: Array<Preference>;
  notifyFirst: number;
  smsTemplateDTO: Array<SmsTemplate>;
  languageList: Array<LanguageKeyMappingDTO>;
  defaultLanguage: number;
  pplBifurcation:string;
}
