import { LanguageDTO } from './language.model';
import { Preference } from './preference.model';

export class GuestDTO {
  guestID: number;
  organizationID: number;
  name: string;
  uuid: string;
  noOfPeople: number;
  noOfChildren: number;
  noOfAdults: number;
  noOfInfants: number;
  partyType: number;
  calloutCount: number;
  deviceId: string;
  deviceType: string;
  contactNo: string;
  email: string;
  prefType: string;
  optin: number;
  rank: number;
  status: string;
  checkinTime: Date;
  seatedTime: Date;
  createdAt: Date;
  modifiedAt: Date;
  incompleteParty: number;
  languagePref: LanguageDTO;
  seatingPreference: Array<Preference>;
  marketingPreference: Array<Preference>;
  note: string;
}
