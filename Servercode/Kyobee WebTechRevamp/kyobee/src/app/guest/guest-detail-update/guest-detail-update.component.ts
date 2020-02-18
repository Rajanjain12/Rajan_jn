import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { GuestDTO } from 'src/app/core/models/guest.model';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { HttpParams } from '@angular/common/http';
import { GuestService } from 'src/app/core/services/guest.service';
import { Preference } from 'src/app/core/models/preference.model';
import { PubNubAngular } from 'pubnub-angular2';
import { environment } from '@env/environment';

declare var $: any;
@Component({
  selector: 'app-guest-detail-update',
  templateUrl: './guest-detail-update.component.html',
  styleUrls: ['./guest-detail-update.component.scss']
})
export class GuestDetailUpdateComponent implements OnInit {
  guest: GuestDTO = new GuestDTO();
  id: string;
  marketingPref: Array<Preference>;
  seatingPref: Array<Preference>;
  errorMessage: string;
  listSeatingPref: Array<Preference>;
  listMarketingPref: Array<Preference>;
  languageKeyMap: Map<String, String>;
  userMetrics: Map<string, string>;
  orgId = 0;

  constructor(private route: ActivatedRoute, private guestService: GuestService, private pubnub: PubNubAngular) {}

  ngOnInit() {
    this.fetchGuest();
  }

  fetchGuest() {
    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id !== null) {
      this.guestService.fetchGuestDetail(this.id).subscribe(res => {
        if (res.success == 1) {
          this.guest = res.serviceResult;
          this.orgId = this.guest.organizationID;
          this.fetchGuestMetric();
          this.fetchOrgPrefandKeyMap();
          console.log(res.serviceResult);
        } else {
          alert(res.message);
        }
      });
    } else {
      alert('invalid url');
    }
  }

  seatingOrMarketingPref() {
    let present = false;
    if (this.listSeatingPref != null) {
      this.listSeatingPref.map(obj => {
        present = false;
        if (this.guest != null && this.guest != undefined) {
          if (this.guest.seatingPreference != null) {
            present = this.guest.seatingPreference.some(el => {
              return el.prefValueId === obj.prefValueId;
            });
          }
        }
        obj.selected = present;
      });
    }

    this.listMarketingPref.map(obj => {
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

    console.log(JSON.stringify(this.listSeatingPref) + ' --- ' + JSON.stringify(this.listMarketingPref));
  }

  resultSeating() {
    this.seatingPref = this.listSeatingPref.filter(seating => seating.selected);
  }

  resultMarketing() {
    this.marketingPref = this.listMarketingPref.filter(marketing => marketing.selected);
  }

  onOptinChange() {
    if (this.guest.optin == 0) {
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

  fetchOrgPrefandKeyMap() {
    const params = new HttpParams().set('orgId', this.guest.organizationID.toString());
    this.guestService.fetchOrgPrefandKeyMap(params).subscribe(res => {
      if (res.success == 1) {
        this.listSeatingPref = res.serviceResult.seatingPreference;
        this.listMarketingPref = res.serviceResult.marketingPreference;
        this.languageKeyMap = res.serviceResult.languageKeyMappingDTO.languageMap;
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
      if (res.success == 1) {
        this.userMetrics = res.serviceResult;
        console.log('user Metrics ' + JSON.stringify(this.userMetrics));
      } else {
        alert(res.message);
      }
    });
  }

  onFormSubmit(form: NgForm) {
    console.log('TCL: GuestDetailUpdateComponent -> onFormSubmit -> form', form);
  }

  onSubmit(invalid) {
    if (invalid) {
      this.errorMessage = 'Please enter proper values ';
      return;
    }
    this.resultSeating();
    this.resultMarketing();

    this.guest.seatingPreference = this.seatingPref;
    this.guest.marketingPreference = this.marketingPref;
    console.log('serating' + JSON.stringify(this.guest));
    this.removeSelected();
    this.guestService.updateGuest(this.guest).subscribe(res => {
      if (res.success == 1) {
        console.log(res);
      } else {
        alert(res.serviceResult);
      }
    });
  }

  connectPubnub() {
    var channel = environment.pubnubIndividualChannel + '_' + this.orgId;
    this.pubnub.init({
      publishKey: environment.pubnubPublishKey,
      subscribeKey: environment.pubnubSubscribeKey
    });
    this.pubnub.addListener({
      message: function(msg) {
        console.log('pusher ' + JSON.stringify(msg));
        if (msg.message.op == 'NOTIFY_USER') {
          if (msg.message.orgId == this.orgId) {
            this.fetchGuestMetric();
          }
        }
        if (
          msg.message.op == 'UPDATE' ||
          msg.message.op == 'ADD' ||
          msg.message.op == 'resetOrganizationPusher' ||
          msg.message.op == 'DELETE' ||
          msg.message.op == 'INCOMPLETE' ||
          msg.message.op == 'NOTPRESENT' ||
          msg.message.op == 'SEATED'
        ) {
          if (msg.message.orgId == this.orgId) {
            this.fetchGuest();
          }
        }
        if (msg.message.op == 'REFRESH_LANGUAGE_PUSHER') {
          this.fetchOrgPrefandKeyMap();
        }
      }
    });
    this.pubnub.subscribe({
      channels: [channel]
    });
  }

  convertMinstoMMHH(min) {
    var h = Math.floor(min / 60);
    var hour = h.toString().length == 1 ? 0 + h.toString() : h;
    var m = min % 60;
    var minute = m.toString().length == 1 ? 0 + m.toString() : m;
    return hour + ':' + minute;
  }
  removeGuest() {
    var params = new HttpParams()
      .set('guestId', this.guest.guestID.toString())
      .set('orgId', this.orgId.toString())
      .set('status', 'DELETE');

    this.guestService.updateGuestStatus(params).subscribe((res: any) => {
      if (res.success == 1) {
        $('#deleteModal').modal('hide');

        this.guest = new GuestDTO();
      } else {
        if (status == 'DELETE') {
          $('#deleteModal').modal('hide');
        }
        alert(res.message);
        this.guest = new GuestDTO();
      }
    });
  }
}
