import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { User } from 'src/app/core/models/user.model';
import { GuestDTO } from 'src/app/core/models/guest.model';
import { Preference } from 'src/app/core/models/preference.model';
import { GuestService } from 'src/app/core/services/guest.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PubNubAngular } from 'pubnub-angular2';
import { environment } from '@env/environment';
import { HttpParams } from '@angular/common/http';
declare var $: any;
@Component({
  selector: 'app-add-guest',
  templateUrl: './add-guest.component.html',
  styleUrls: ['./add-guest.component.scss']
})
export class AddGuestComponent implements OnInit {
  guest: GuestDTO = new GuestDTO();
  user: User = new User();
  errorMessage: string;
  public sum = 0;
  marketingPref: Array<Preference>;
  seatingPref: Array<Preference>;
  id: string;
  languageKeyMap: Map<string, string>;
  selectedItem;
  languageList = [];
  listSeatingPref: Array<Preference>;
  listMarketingPref: Array<Preference>;

  constructor(
    private authService: AuthService,
    private guestService: GuestService,
    private route: ActivatedRoute,
    private router: Router,
    private pubnub: PubNubAngular
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.user = this.authService.getUser();
    console.log('user' + JSON.stringify(this.user));
    this.guest = new GuestDTO();
    this.connectPubnub();

    console.log('seating pref ' + JSON.stringify(this.user.seatingpref));
    this.languageList = this.user.languagePref;
    console.log('language List:' + this.languageList);
    this.selectedItem = this.languageList.find(x => x.langIsoCode === 'en');
    console.log('default language' + JSON.stringify(this.selectedItem));
    this.selectedLanguage();
    if (this.id !== null) {
      this.guestService.fetchGuest(this.id).subscribe(res => {
        console.log('res' + JSON.stringify(res));
        if (res.success === 1) {
          this.guest = res.serviceResult;
          this.seatingOrMarketingPref();
          console.log('result ' + JSON.stringify(res.serviceResult));
        }
      });
      console.log(this.id);
    } else {
      this.guest.guestID = 0;
      this.guest.optin = 0;
      this.seatingOrMarketingPref();
      console.log('id is absent');
      console.log(this.guest);
    }

    // default lang settings
  }

  seatingOrMarketingPref() {
    console.log('user:' + JSON.stringify(this.guest));
    let present = false;
    if (this.user.seatingpref !== null) {
      this.user.seatingpref.map(obj => {
        present = false;
        if (this.guest !== null && this.guest !== undefined) {
          if (this.guest.seatingPreference !== null && this.guest.seatingPreference !== undefined) {
            present = this.guest.seatingPreference.some(el => {
              return el.prefValueId === obj.prefValueId;
            });
          }
        }
        obj.selected = present;
      });
    }

    this.user.marketingPref.map(obj => {
      let present = false;
      if (this.guest != null && this.guest !== undefined) {
        if (this.guest.marketingPreference != null) {
          present = this.guest.marketingPreference.some(el => {
            return el.prefValueId === obj.prefValueId;
          });
        }
      }

      obj.selected = present;
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
    if (this.guest.optin === 0) {
      this.guest.optin = 1;
    } else {
      this.guest.optin = 0;
    }
  }

  selectedLanguage() {
    console.log('lang:' + JSON.stringify(this.selectedItem));

    this.languageKeyMap = this.selectedItem.languageMap;
    console.log('language map:' + JSON.stringify(this.languageKeyMap));

    this.listSeatingPref = this.user.seatingpref;
    console.log('seating list:' + JSON.stringify(this.listSeatingPref));
    this.listMarketingPref = this.user.marketingPref;
    console.log('marketing list:' + JSON.stringify(this.listMarketingPref));

    this.guest.languagePref = {
      langID: this.selectedItem.langId,
      keyName: null,
      value: null,
      langIsoCode: this.selectedItem.langIsoCode,
      langName: this.selectedItem.langName
    };
    console.log('language pref ' + JSON.stringify(this.guest.languagePref));
  }

  addGuest() {
    this.guest.incompleteParty = 0;
    this.guest.organizationID = this.user.organizationID;
    this.guest.partyType = -1;
    this.guest.prefType = null;
    this.guest.rank = 0;
    this.guest.seatedTime = null;
    this.guest.status = 'CHECKIN';
    this.guest.modifiedAt = null;
    this.guest.uuid = null;
    this.guest.calloutCount = 0;
    this.guest.checkinTime = new Date();
    this.guest.createdAt = new Date();
    this.guest.deviceId = null;
    this.guest.deviceType = null;
    this.guest.email = null;

    this.guest.incompleteParty = 0;

    console.log(' updatee guest ' + JSON.stringify(this.guest));
  }

  validate(invalid) {
    if (invalid) {
      this.errorMessage = this.languageKeyMap['upd_error'];
      return;
    }
    $('#btnSubmit').attr('disabled', true);
    this.resultSeating();
    this.resultMarketing();
    this.removeSelected();
    this.guest.noOfInfants = 0;
    if (this.guest.noOfChildren == null) {
      this.guest.noOfChildren = 0;
    }
    if (this.guest.noOfPeople == null) {
      this.guest.noOfPeople = 0;
    }
    if (this.guest.noOfAdults !== null && this.guest.noOfAdults !== undefined) {
      this.guest.noOfPeople = this.guest.noOfAdults + this.guest.noOfChildren;
    }
    if (this.guest.noOfAdults == null) {
      this.guest.noOfAdults = 0;
    }
    console.log('no of people:' + this.guest.noOfPeople);
    // this.guest.noOfPeople = +this.guest.noOfAdults + +this.guest.noOfChildren;
    if (this.sum > this.user.maxParty) {
      this.errorMessage =
        this.languageKeyMap['org_max_party_1'] + this.user.maxParty + this.languageKeyMap['org_max_party_2'];
      return;
    } else {
      console.log(this.guest.noOfPeople);
    }
    this.guest.seatingPreference = this.seatingPref;
    this.guest.marketingPreference = this.marketingPref;
    if (this.guest.guestID === 0) {
      this.addGuest();
      console.log('guest' + JSON.stringify(this.guest));
      this.guestService.addGuest(this.guest).subscribe(res => {
        if (res.success === 1) {
          console.log(res);
          this.router.navigateByUrl('/waitlist/dashboard');
        } else {
          alert(res.message);
        }
      });
    } else {
      console.log('child:' + this.guest.noOfChildren);
      console.log('adults' + this.guest.noOfAdults);

      console.log('guest update' + JSON.stringify(this.guest));
      this.guestService.updateGuest(this.guest).subscribe(res => {
        if (res.success === 1) {
          console.log(res);
          this.router.navigateByUrl('/waitlist/dashboard');
        } else {
          alert(res.serviceResult);
        }
      });
    }
  }
  fetchLanguageKeyMap() {
    const params = new HttpParams().set('orgId', this.user.organizationID.toString());

    this.guestService.fetchLanguageKeyMap(params).subscribe(res => {
      if (res.success === 1) {
        this.languageList = res.serviceResult;
        this.user.languagePref = this.languageList;
        this.user.languagePref.map(lang => {
          if (this.selectedItem.langId === lang.langId) {
            this.selectedItem = lang;
          }
        });
        this.selectedLanguage();
      } else {
        alert(res.serviceResult);
      }
    });
  }

  connectPubnub() {
    const channel = environment.pubnuGlobalChannel;
    this.pubnub.init({
      publishKey: environment.pubnubPublishKey,
      subscribeKey: environment.pubnubSubscribeKey
    });
    this.pubnub.addListener({
      message: msg => {
        console.log('pusher ' + JSON.stringify(msg));
        if (msg.message.op === 'REFRESH_LANGUAGE_PUSHER') {
          this.fetchLanguageKeyMap();
        }
      }
    });
    this.pubnub.subscribe({
      channels: [channel]
    });
  }
}
