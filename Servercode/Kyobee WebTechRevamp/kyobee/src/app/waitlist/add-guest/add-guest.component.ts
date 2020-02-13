import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { User } from 'src/app/core/models/user.model';
import { GuestDTO } from 'src/app/core/models/guest.model';
import { Preference } from 'src/app/core/models/preference.model';
import { GuestService } from 'src/app/core/services/guest.service';

@Component({
  selector: 'app-add-guest',
  templateUrl: './add-guest.component.html',
  styleUrls: ['./add-guest.component.scss']
})
export class AddGuestComponent implements OnInit {

  guest : GuestDTO = new GuestDTO();
  user : User = new User();
  errorMessage : string;
  public sum : number;
  marketingPref:Array<Preference>;
  seatingPref: Array<Preference>;
  removeIndex;
  
  constructor(private authService: AuthService, private guestService: GuestService) { }

  counter(i: number) {
    return  Array(i);
  }
  
  ngOnInit() {
  this.user =  this.authService.getUser();
   this.user.seatingpref.map((obj) => {
    obj.selected = false;
  })
   this.user.marketingPref.map((obj) => {
    obj.selected = false;
  })
    console.log("seatingpref=="+JSON.stringify(this.user.seatingpref)); 
    console.log("marketingPref=="+JSON.stringify(this.user.marketingPref));
}

  get resultSeating() {
   return this.seatingPref =  this.user.seatingpref.filter(seating => seating.selected);
  }

  get resultMarketing() {
    return this.marketingPref = this.user.marketingPref.filter(marketing => marketing.selected);
  }

  removeSelected(){
    this.seatingPref.map((obj)=>{
      delete obj.selected;
    });
    this.marketingPref.map((obj)=>{
      delete obj.selected;
    });
  }

  addGuest(){
    this.removeSelected();
    this.guest.incompleteParty=0;
    this.guest.organizationID=this.user.organizationID;
    this.guest.partyType=-1;
    this.guest.prefType=null;
    this.guest.rank=0;
    this.guest.seatedTime=null;
    this.guest.status=null;
    this.guest.updatedTime=null;
    this.guest.uuid=null;
    this.guest.calloutCount=0;
    this.guest.checkinTime=null;
    this.guest.createdTime=null;
    this.guest.deviceId=null;
    this.guest.deviceType=null;
    this.guest.email=null;
    this.guest.guestID=0;
    this.guest.incompleteParty=0;
    this.guest.seatingPreference = this.seatingPref;
    this.guest.marketingPreference = this.marketingPref;
    this.guest.langguagePref = {     
      "langId": 1,
      "keyName": null,
      "value": null,
      "langIsoCode": "en",
      "langName": "English",
    };
    this.guest.optin=0;  
    console.log(JSON.stringify(this.guest));
    console.log("seatingpref=="+JSON.stringify(this.resultSeating));
    console.log("marketingPref=="+JSON.stringify(this.resultMarketing));
  }

  validate(invalid){
    if(invalid){
      this.errorMessage = "Please enter proper values " 
      return
    }
    console.log("jaaaam"); 
    console.log("seatingpref=="+JSON.stringify(this.resultSeating)); 
    console.log("marketingPref=="+JSON.stringify(this.resultMarketing)); 
   this.guest.noOfPeople = +this.guest.noOfAdults + +this.guest.noOfChildren;
    if(this.sum > this.user.maxParty){
      this.errorMessage = "More than " +this.user.maxParty+ " people are not allowed";
      return
    } else {
    console.log(this.sum);
    }
    this.addGuest();
    this.guestService.addGuest(this.guest).subscribe((res)=>{
      if(res.success == 1){
        console.log(res);
      }
    })
  }   
}
/* for (var i = 0; i < user.seatingpref.length; i++) {
    user.seatingpref[i] = Object.assign(user.seatingpref[i], {
      selected: false, 
  });
}*/