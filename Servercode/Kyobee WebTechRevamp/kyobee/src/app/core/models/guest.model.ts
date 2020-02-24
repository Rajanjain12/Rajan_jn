import { LanguageDTO } from './language.model';
import { Preference } from './preference.model';

export class GuestDTO {
  guestID: number;
  organizationID: number;
  name: String;
  uuid: String;
  noOfPeople: number;
  noOfChildren: number;
  noOfAdults: number;
  noOfInfants: number;
  partyType: number;
  calloutCount: number;
  deviceId: string;
  deviceType: string;
  contactNo: String;
  email: string;
  prefType: String;
  optin: number;
  rank: number;
  status: string;
  checkinTime: Date;
  seatedTime: Date;
  createdTime: Date;
  updatedTime: Date;
  incompleteParty: number;
  languagePref: LanguageDTO;
  seatingPreference: Array<Preference>;
  marketingPreference: Array<Preference>;
  note: string;
}
