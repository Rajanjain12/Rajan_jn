import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { User } from 'src/app/core/models/user.model';
import { GuestDTO } from 'src/app/core/models/guest.model';
import { Preference } from 'src/app/core/models/preference.model';
import { GuestService } from 'src/app/core/services/guest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-add-guest',
  templateUrl: './add-guest.component.html',
  styleUrls: ['./add-guest.component.scss']
})
export class AddGuestComponent implements OnInit {
  guest: GuestDTO = new GuestDTO();
  user: User = new User();
  errorMessage: string;
  public sum: number = 0;
  marketingPref: Array<Preference>;
  seatingPref: Array<Preference>;
  id: string;
  languageKeyMap: Map<string, string>;
  selectedItem;
  defaultLanguage = [];
  listSeatingPref: Array<Preference>;
  listMarketingPref: Array<Preference>;

  constructor(
    private authService: AuthService,
    private guestService: GuestService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  counter(i: number) {
    return Array(i);
  }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id !== null) {
      this.guestService.fetchGuest(this.id).subscribe(res => {
        console.log('res' + JSON.stringify(res));
        if (res.success == 1) {
          this.guest = res.serviceResult;

          console.log(res.serviceResult);
        }
      });
      console.log(this.id);
    } else {
      this.guest = new GuestDTO();
      this.guest.guestID = 0;
      this.guest.optin = 0;

      console.log('id is absent');
      console.log(this.guest);
    }

    //default lang settings
    this.user = this.authService.getUser();
    this.defaultLanguage = this.user.languagePref;
    this.selectedItem = this.defaultLanguage.find(x => x.langIsoCode === 'en');
    console.log("default language" + JSON.stringify(this.selectedItem));
    this.selectedLanguage();
  }

  seatingOrMarketingPref() {
    this.user = this.authService.getUser();
    console.log('user:' + JSON.stringify(this.authService.getUser()));
    let present = false;
    if (this.user.seatingpref != null) {
      this.user.seatingpref.map(obj => {
        present = false;
        if (this.guest != null && this.guest != undefined) {
          if (this.guest.seatingPreference != null) {
            present = this.guest.seatingPreference.some(el => {
              return el.prefValueId === obj.prefValueId;
            });
          }
        }
        obj.prefValue = this.languageKeyMap[obj.prefKey != null ? obj.prefKey : 0];
        obj.selected = present;
      });
    }

    this.user.marketingPref.map(obj => {
      let present = false;
      if (this.guest != null && this.guest != undefined) {
        if (this.guest.marketingPreference != null) {
          present = this.guest.marketingPreference.some(el => {
            return el.prefValueId === obj.prefValueId;
          });
        }
      }
      if (present) {
        obj.selected = true;
      } else {
        obj.selected = false;
      }
    });
  }

  resultSeating() {
    this.seatingPref = this.user.seatingpref.filter(seating => seating.selected);
  }

  resultMarketing() {
    this.marketingPref = this.user.marketingPref.filter(marketing => marketing.selected);
  }

  removeSelected() {
    this.seatingPref.map(obj => {
      delete obj.selected;
    });
    this.marketingPref.map(obj => {
      delete obj.selected;
    });
  }

  onOptinChange() {
    if (this.guest.optin == 0) {
      this.guest.optin = 1;
    } else {
      this.guest.optin = 0;
    }
  }

  selectedLanguage() {
    console.log('lang:' + JSON.stringify(this.selectedItem));

    this.languageKeyMap = this.selectedItem.languageMap;
    console.log('language map:' + JSON.stringify(this.languageKeyMap));
    //  this.seatingOrMarketingPref();
    this.listSeatingPref = this.user.seatingpref;
    this.listMarketingPref = this.user.marketingPref;

    this.guest.languagePref = {
      langId: this.selectedItem.langId,
      keyName: null,
      value: null,
      langIsoCode: this.selectedItem.langIsoCode,
      langName: this.selectedItem.langName
    };
  }

  addGuest() {
    //this.onOptinChange();
    this.guest.incompleteParty = 0;
    this.guest.organizationID = this.user.organizationID;
    this.guest.partyType = -1;
    this.guest.prefType = null;
    this.guest.rank = 0;
    this.guest.seatedTime = null;
    this.guest.status = 'CHECKIN';
    this.guest.updatedTime = null;
    this.guest.uuid = null;
    this.guest.calloutCount = 0;
    this.guest.checkinTime = new Date();
    this.guest.createdTime = new Date();
    this.guest.deviceId = null;
    this.guest.deviceType = null;
    this.guest.email = null;

    //  this.guest.guestID=0;
    this.guest.incompleteParty = 0;

    /* this.guest.languagePref = {
      langId: 1,
      keyName: null,
      value: null,
      langIsoCode: 'en',
      langName: 'English'
    };*/
    console.log(' updatee guest ' + JSON.stringify(this.guest));
  }

  validate(invalid) {
    if (invalid) {
      this.errorMessage = 'Please enter proper values ';
      return;
    }
    this.resultSeating();
    this.resultMarketing();
    this.removeSelected();
    this.guest.noOfInfants = 0;
    if (this.guest.noOfChildren == null) {
      this.guest.noOfChildren = 0;
    }
    this.guest.noOfPeople = +this.guest.noOfAdults + +this.guest.noOfChildren;
    if (this.sum > this.user.maxParty) {
      this.errorMessage = 'More than ' + this.user.maxParty + ' people are not allowed';
      return;
    } else {
      console.log(this.guest.noOfPeople);
    }
    this.guest.seatingPreference = this.seatingPref;
    this.guest.marketingPreference = this.marketingPref;
    if (this.guest.guestID == 0) {
      this.addGuest();
      this.guestService.addGuest(this.guest).subscribe(res => {
        if (res.success == 1) {
          console.log(res);
          this.router.navigateByUrl('/waitlist/dashboard');
        } else {
          alert(res.message);
        }
      });
    } else {
      this.guestService.updateGuest(this.guest).subscribe(res => {
        if (res.success == 1) {
          console.log(res);
          this.router.navigateByUrl('/waitlist/dashboard');
        } else {
          alert(res.serviceResult);
        }
      });
    }
  }
}
