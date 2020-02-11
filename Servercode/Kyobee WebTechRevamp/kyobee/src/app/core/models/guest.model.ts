import { LanguageDTO } from './language.model';

export class GuestDTO{
     guestID:number;
     organizationID:number;
     name:String;
     uuid:String;
     noOfPeople:number;
     noOfChildren:number;
     noOfAdults:number;
     noOfInfants:number;
     partyType:number;
     calloutCount:number;
     deviceId:string;
     deviceType:string;
     sms:String;
     email:string;
     prefType:String;
     optin:number;
     rank:number;
     status:string;
     checkinTime:Date;
     seatedTime:Date;
     createdTime:Date;
     updatedTime:Date;
     incompleteParty:number;
     langguagePref:LanguageDTO;
     seatingPreference:string;
     marketingPreference:string;
     note:string;
}