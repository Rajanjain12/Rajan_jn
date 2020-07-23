import { Preference } from './preference.model';
import { SmsTemplate } from './sms-template.model';
import { OrgUserDetailsDTO } from './orguser-details.model';

export class User {
  userID: number;
  userName: string;
  firstName: string;
  lastName: string;
  email: string;
  permissionList: null;
  organizationID: number;
  smsRoute: string;
  maxParty: number;
  defaultLangId: number;
  clientBase: string;
  companyEmail: string;
  organizationName: string;
  seatingpref: Array<Preference>;
  marketingPref: Array<Preference>;
  smsTemplate: Array<SmsTemplate>;
  languagePref: any[];
  footerMsg: string;
  logoPath: string;
  notifyFirst: number;
  pplBifurcation: string;
  customerId: number;
  orgUserDetailList: Array<OrgUserDetailsDTO>;
}
