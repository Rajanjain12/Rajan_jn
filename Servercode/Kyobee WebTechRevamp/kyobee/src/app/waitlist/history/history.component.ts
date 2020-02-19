import { Component, OnInit } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { GuestDTO } from 'src/app/core/models/guest.model';
import { GuestService } from 'src/app/core/services/guest.service';
import { PubNubAngular } from 'pubnub-angular2';
import { environment } from '@env/environment';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent implements OnInit {
  constructor(private guestService: GuestService, private pubnub: PubNubAngular) {}
  statusOptions = ['All', 'Not Present', 'Incomplete'];
  selectedStatus: string;
  orgId;
  pageNo;
  pageSize;
  searchText;
  sliderMaxTime = 24;
  sliderMinTime = 0;
  guestDTOList: Array<GuestDTO>;
  toggleColumnArr = {
    action: true,
    rank: false,
    noOfParty: true,
    name: true,
    checkInTime: true,
    note: false,
    languagePref: false,
    optIn: false
  };

  ngOnInit() {
    this.selectedStatus = this.statusOptions[0];
    this.orgId = 2;
    this.pageNo = 0;
    this.pageSize = 10;
    this.searchText = null;
    this.pageNo = this.pageNo + 1;
    this.connectPubnub();
    this.fetchGuestHistory();
  }

  optionChange(status) {
    this.selectedStatus = status;
  }

  pad(value) {
    return value < 10 ? '0' + value : value;
  }
  createOffset(date) {
    var sign = date.getTimezoneOffset() > 0 ? '-' : '+';
    var offset = Math.abs(date.getTimezoneOffset());
    var hours = this.pad(Math.floor(offset / 60));
    var minutes = this.pad(offset % 60);

    return sign + hours + ':' + minutes;
  }

  fetchGuestHistory() {
    var d = new Date();
    var tzName = this.createOffset(d).toString();
    //alert(encodeURIComponent(tzName));
    var params = new HttpParams()
      .set('orgId', this.orgId)
      .set('pageNo', this.pageNo)
      .set('pageSize', this.pageSize)
      .set('searchText', this.searchText)
      .set('clientTimezone', '%2B05%3A30')
      .set('sliderMaxTime', this.sliderMaxTime.toString())
      .set('sliderMinTime', this.sliderMinTime.toString())
      .set('statusOption', this.selectedStatus);
    // alert(params);
    this.guestService.fetchGuestHistoryList(params).subscribe((res: any) => {
      if (res.success == 1) {
        this.guestDTOList = res.serviceResult.records;
        console.log('user==' + JSON.stringify(res.serviceResult));
        // this.selectedGuest=new GuestDTO();
      } else {
        alert(res.message);
      }
    });
  }

  toggleColumns(status) {
    switch (status) {
      case 'ACTION':
        if (this.toggleColumnArr.action == true) {
          this.toggleColumnArr.action = false;
        } else {
          this.toggleColumnArr.action = true;
        }
        break;

      case 'RANK':
        if (this.toggleColumnArr.rank == true) {
          this.toggleColumnArr.rank = false;
        } else {
          this.toggleColumnArr.rank = true;
        }
        break;

      case 'NOOFPARTY':
        if (this.toggleColumnArr.noOfParty == true) {
          this.toggleColumnArr.noOfParty = false;
        } else {
          this.toggleColumnArr.noOfParty = true;
        }
        break;

      case 'NAME':
        if (this.toggleColumnArr.name == true) {
          this.toggleColumnArr.name = false;
        } else {
          this.toggleColumnArr.name = true;
        }
        break;

      case 'CHECKIN':
        if (this.toggleColumnArr.checkInTime == true) {
          this.toggleColumnArr.checkInTime = false;
        } else {
          this.toggleColumnArr.checkInTime = true;
        }
        break;

      case 'NOTE':
        if (this.toggleColumnArr.note == true) {
          this.toggleColumnArr.note = false;
        } else {
          this.toggleColumnArr.note = true;
        }
        break;

      case 'LANGUAGEPREF':
        if (this.toggleColumnArr.languagePref == true) {
          this.toggleColumnArr.languagePref = false;
        } else {
          this.toggleColumnArr.languagePref = true;
        }
        break;

      case 'OPTIN':
        if (this.toggleColumnArr.optIn == true) {
          this.toggleColumnArr.optIn = false;
        } else {
          this.toggleColumnArr.optIn = true;
        }
        break;
    }
  }

  fetchNextGuest() {
    this.pageNo = this.pageNo + 1;
    this.fetchGuestHistory();
  }

  fetchPrevGuest() {
    this.pageNo = this.pageNo - 1;
    this.fetchGuestHistory();
  }
  onSliderChange(selectedValues: number[]) {
    //this. = selectedValues.values;
    console.log(JSON.stringify(selectedValues));
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
            this.fetchOrgMetrics();
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
      }
    });
    this.pubnub.subscribe({
      channels: [channel]
    });
  }
}
