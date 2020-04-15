import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { GuestDTO } from 'src/app/core/models/guest.model';
import { ActivatedRoute } from '@angular/router';
import { HttpParams } from '@angular/common/http';
import { GuestService } from 'src/app/core/services/guest.service';
import { Preference } from 'src/app/core/models/preference.model';
import { PubNubAngular } from 'pubnub-angular2';
import { environment } from '@env/environment';
import { User } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';

declare var $: any;
@Component({
  selector: 'app-guest-detail-update',
  templateUrl: './guest-detail-update.component.html',
  styleUrls: ['./guest-detail-update.component.scss']
})
export class GuestDetailUpdateComponent implements OnInit {
  guest: GuestDTO;
  id: string;
  marketingPref: Array<Preference>;
  seatingPref: Array<Preference>;
  errorMessage: string;
  listSeatingPref: Array<Preference>;
  listMarketingPref: Array<Preference>;
  languageKeyMap: Map<string, string>;
  userMetrics: Map<string, string>;
  orgId = 0;
  user: User = new User();

  constructor(
    private route: ActivatedRoute,
    private guestService: GuestService,
    private pubnub: PubNubAngular,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.user = this.authService.getUser();
    const promise = this.fetchGuest();

    promise.then(value => {
      this.connectPubnub();
    });
  }

  fetchGuest() {
    var promise = (promise = new Promise((resolve, reject) => {
      this.id = this.route.snapshot.paramMap.get('id');
      if (this.id !== null) {
        this.guestService.fetchGuestDetail(this.id).subscribe(res => {
          if (res.success === 1) {
            this.languageKeyMap = res.serviceResult.languageKeyMap;
            this.guest = res.serviceResult;
            this.orgId = this.guest.organizationID;
            resolve();
            this.fetchGuestMetric();
            this.fetchOrgPref();

            console.log(res.serviceResult);
          } else {
            reject();
            this.errorMessage = res.message;
          }
        });
      } else {
        reject();
        alert('invalid url');
      }
    }));

    return promise;
  }

  seatingOrMarketingPref() {
    let present = false;
    if (this.listSeatingPref != null) {
      this.listSeatingPref.map(obj => {
        present = false;
        if (this.guest != null && this.guest !== undefined) {
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

    this.listMarketingPref.map(obj => {
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

    console.log(JSON.stringify(this.listSeatingPref) + ' --- ' + JSON.stringify(this.listMarketingPref));
  }

  resultSeating() {
    this.seatingPref = this.listSeatingPref.filter(seating => seating.selected);
  }

  resultMarketing() {
    this.marketingPref = this.listMarketingPref.filter(marketing => marketing.selected);
  }

  onOptinChange() {
    if (this.guest.optin === 0) {
      this.guest.optin = 1;
    } else {
      this.guest.optin = 0;
    }
  }

  removeSelected() {
    this.seatingPref.map(obj => {
      delete obj.selected;
    });
    this.marketingPref.map(obj => {
      delete obj.selected;
    });
  }
  showDeleteModal() {
    $('#deleteModal').modal('show');
  }
  fetchOrgPref() {
    const params = new HttpParams().set('orgId', this.guest.organizationID.toString());
    this.guestService.fetchOrgPref(params).subscribe(res => {
      if (res.success === 1) {
        this.listSeatingPref = res.serviceResult.seatingPreference;
        this.listMarketingPref = res.serviceResult.marketingPreference;

        console.log(
          JSON.stringify(this.listSeatingPref) +
            ' --- ' +
            JSON.stringify(this.listMarketingPref) +
            ' ---- ' +
            JSON.stringify(this.languageKeyMap)
        );
        this.seatingOrMarketingPref();
      } else {
        alert(res.message);
      }
    });
  }

  fetchGuestMetric() {
    const params = new HttpParams()
      .set('orgId', this.guest.organizationID.toString())
      .set('guestId', this.guest.guestID.toString());
    this.guestService.fetchGuestMetrics(params).subscribe(res => {
      if (res.success === 1) {
        this.userMetrics = res.serviceResult;
        console.log('user Metrics ' + JSON.stringify(this.userMetrics));
      } else {
        alert(res.message);
      }
    });
  }

  onSubmit(invalid) {
    if (invalid) {
      this.errorMessage = this.languageKeyMap['upd_error'];
      return;
    }
    this.resultSeating();
    this.resultMarketing();

    this.guest.seatingPreference = this.seatingPref;
    this.guest.marketingPreference = this.marketingPref;
    console.log('serating' + JSON.stringify(this.guest));
    this.removeSelected();
    this.guestService.updateGuest(this.guest).subscribe(res => {
      if (res.success === 1) {
        console.log(res);
      } else {
        alert(res.serviceResult);
      }
    });
  }

  connectPubnub() {
    console.log('org Id ' + this.orgId);
    const channel = environment.pubnubIndividualChannel + '_' + this.orgId;

    this.pubnub.init({
      publishKey: environment.pubnubPublishKey,
      subscribeKey: environment.pubnubSubscribeKey
    });
    this.pubnub.addListener({
      message: msg => {
        console.log('pusher ' + JSON.stringify(msg));
        if (msg.message.op === 'NOTIFY_USER') {
          if (msg.message.orgId === this.orgId) {
            this.fetchGuestMetric();
          }
        }
        if (
          msg.message.op === 'UPDATE' ||
          msg.message.op === 'ADD' ||
          msg.message.op === 'RESET_ORGANIZATION_PUSHER' ||
          msg.message.op === 'DELETE' ||
          msg.message.op === 'INCOMPLETE' ||
          msg.message.op === 'NOTPRESENT' ||
          msg.message.op === 'SEATED'
        ) {
          if (msg.message.orgId === this.orgId) {
            this.fetchGuest();
          }
        }
        if (msg.message.op === 'REFRESH_LANGUAGE_PUSHER') {
          this.fetchOrgPref();
          this.fetchGuest();
        }
      }
    });
    this.pubnub.subscribe({
      channels: [channel]
    });
  }

  convertMinstoMMHH(min) {
    const h = Math.floor(min / 60);
    const hour = h.toString().length === 1 ? 0 + h.toString() : h;
    const m = min % 60;
    const minute = m.toString().length === 1 ? 0 + m.toString() : m;
    return hour + ':' + minute;
  }
  hideDeletePopup() {
    $('#deleteModal').modal('hide');
  }
  removeGuest() {
    const params = new HttpParams()
      .set('guestId', this.guest.guestID.toString())
      .set('orgId', this.orgId.toString())
      .set('status', 'DELETE');

    this.guestService.updateGuestStatus(params).subscribe((res: any) => {
      if (res.success === 1) {
        $('#deleteModal').modal('hide');

        this.guest = new GuestDTO();
      } else {
        if (status === 'DELETE') {
          $('#deleteModal').modal('hide');
        }
        alert(res.message);
        this.guest = new GuestDTO();
      }
    });
  }
}
