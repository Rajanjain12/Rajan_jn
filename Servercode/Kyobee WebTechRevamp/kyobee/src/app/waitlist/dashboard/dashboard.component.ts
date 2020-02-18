import { Component, OnInit } from '@angular/core';
import { GuestService } from 'src/app/core/services/guest.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { HttpParams } from '@angular/common/http';
import { GuestDTO } from 'src/app/core/models/guest.model';
import { OrganizationService } from 'src/app/core/services/organization.service';
import { OrganizationMetrics } from 'src/app/core/models/organization-metrics.model';
import { SmsContentDTO } from 'src/app/core/models/sms-content.model';
import { OrganizationTemplateDTO } from 'src/app/core/models/organization-template.model';
import { PubNubAngular } from 'pubnub-angular2';
import { environment } from '@env/environment';
import { User } from 'src/app/core/models/user.model';

declare var $: any;
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  constructor(
    private guestService: GuestService,
    private organizationService: OrganizationService,
    private authService: AuthService,
    private pubnub: PubNubAngular
  ) {}
  user: User;
  orgId;
  pageNo;
  pageSize;
  searchText;
  guestDTOList: Array<GuestDTO>;
  selectedGuest: GuestDTO;
  organizationMetrics: OrganizationMetrics;
  organizationTemplateDTOList: Array<OrganizationTemplateDTO>;
  waitTime: any = null;
  waitTimeOption = [
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8,
    9,
    10,
    11,
    12,
    13,
    14,
    15,
    16,
    17,
    18,
    19,
    20,
    21,
    22,
    23,
    24,
    25,
    26,
    27,
    28,
    29,
    30,
    31,
    32,
    33,
    34,
    35,
    36,
    37,
    38,
    39,
    40,
    41,
    42,
    43,
    44,
    45,
    46,
    47,
    48,
    49,
    50,
    51,
    52,
    53,
    54,
    55,
    56,
    57,
    58,
    59,
    60,
    61,
    62,
    63,
    64,
    65,
    66,
    67,
    68,
    69,
    70,
    71,
    72,
    73,
    74,
    75,
    76,
    77,
    78,
    79,
    80,
    81,
    82,
    83,
    84,
    85,
    86,
    87,
    88,
    89,
    90,
    91,
    92,
    93,
    94,
    95,
    96,
    97,
    98,
    99
  ];
  smsContentDTO: SmsContentDTO = new SmsContentDTO();
  dashboardIlluImageSrc = '../../../assets/images/dashboard-illu.png';
  notPresentImg = '../../../assets/images/not-present.png';
  noteIconImg = '../../../assets/images/note-icon.png';
  deleteIconImg = '../../../assets/images/delete-icon.png';
  msgIconImg = '../../../assets/images/msg-icon.png';
  roundarrow = '../../../assets/images/roundarrow.png';
  toggleColumnArr = {
    action: true,
    rank: false,
    noOfParty: true,
    name: true,
    checkInTime: true,
    note: false,
    deleteSym: true,
    sendText: true,
    languagePref: false,
    optIn: false
  };

  ngOnInit() {
    this.user = this.authService.getUser();
    this.orgId = 2;
    this.pageNo = 0;
    this.pageSize = 10;
    this.searchText = null;
    this.pageNo = this.pageNo + 1;
    this.connectPubnub();
    this.fetchOrgMetrics();
    this.fetchGuest();
  }

  fetchGuest() {
    var params = new HttpParams()
      .set('orgId', this.orgId)
      .set('pageNo', this.pageNo)
      .set('pageSize', this.pageSize)
      .set('searchText', this.searchText);
    this.guestService.fetchGuestList(params).subscribe((res: any) => {
      if (res.success == 1) {
        this.guestDTOList = res.serviceResult.records;
        console.log('user==' + JSON.stringify(this.guestDTOList));
        this.selectedGuest = new GuestDTO();
      } else {
        alert(res.message);
      }
    });
  }

  fetchOrgMetrics() {
    var params = new HttpParams().set('orgId', this.orgId);
    this.organizationService.fetchOrganizationMetrics(params).subscribe((res: any) => {
      if (res.success == 1) {
        this.organizationMetrics = res.serviceResult;
        this.waitTime = res.serviceResult.perPartyWaitTime;
        console.log('org == ' + JSON.stringify(this.organizationMetrics));
      } else {
        alert(res.message);
      }
    });
  }

  saveOrgMetrics() {
    var params = new HttpParams()
      .set('orgId', this.orgId)
      .set('perPartyWaitTime', this.waitTime)
      .set('numberOfUsers', this.organizationMetrics.notifyUserCount.toString());
    this.organizationService.saveOrganizationMetrics(params).subscribe((res: any) => {
      if (res.success == 1) {
        console.log('org == ' + JSON.stringify(res));
        this.organizationMetrics = res.serviceResult.waitlistMetrics;
        this.organizationMetrics.orgTotalWaitTime = res.serviceResult.waitlistMetrics.totalWaitTime;
      } else {
        alert(res.message);
      }
    });
  }

  showSeatModal(guest) {
    this.selectedGuest = guest;
    $('#seatModal').modal('show');
  }

  showDeleteModal(guest) {
    this.selectedGuest = guest;
    $('#deleteModal').modal('show');
  }

  removeSelected(status) {
    if (status == 'DELETE') {
      $('#deleteModal').modal('hide');
    } else if (status == 'SEATMODAL') {
      $('#seatModal').modal('hide');
    } else if (status == 'SMSMODAL') {
      $('#smsModal').modal('hide');
    }
    this.selectedGuest = new GuestDTO();
  }

  changeStatus(status) {
    var params = new HttpParams()
      .set('guestId', this.selectedGuest.guestID.toString())
      .set('orgId', this.orgId)
      .set('status', status);

    this.guestService.updateGuestStatus(params).subscribe((res: any) => {
      if (res.success == 1) {
        if (status == 'DELETE') {
          $('#deleteModal').modal('hide');
        } else {
          $('#seatModal').modal('hide');
        }

        alert(res.serviceResult);

        this.selectedGuest = new GuestDTO();
      } else {
        if (status == 'DELETE') {
          $('#deleteModal').modal('hide');
        } else {
          $('#seatModal').modal('hide');
        }
        alert(res.message);
        this.selectedGuest = new GuestDTO();
      }
    });
  }

  fetchSMSContent(guest) {
    this.smsContentDTO = new SmsContentDTO();
    this.selectedGuest = guest;
    this.smsContentDTO.orgId = 1;
    this.smsContentDTO.guestId = this.selectedGuest.guestID;
    this.smsContentDTO.clientBase = 'admin';
    this.smsContentDTO.guestName = this.selectedGuest.name;
    this.smsContentDTO.guestRank = this.selectedGuest.rank;
    this.smsContentDTO.guestUuid = this.selectedGuest.uuid;
    this.smsContentDTO.langId = this.selectedGuest.languagePref.langId;
    this.smsContentDTO.tempLevel = 1;
    alert(JSON.stringify(this.smsContentDTO));
    this.organizationService.fetchSmsContent(this.smsContentDTO).subscribe((res: any) => {
      if (res.success == 1) {
        this.organizationTemplateDTOList = res.serviceResult;
        console.log(JSON.stringify(this.organizationTemplateDTOList));
        $('#smsModal').modal('show');
      } else {
        alert(res.serviceResult);
      }
    });
  }

  fetchNextGuest() {
    this.pageNo = this.pageNo + 1;
    this.fetchGuest();
  }

  fetchPrevGuest() {
    this.pageNo = this.pageNo - 1;
    this.fetchGuest();
  }

  convertMinstoMMHH(min) {
    var h = Math.floor(min / 60);
    var hour = h.toString().length == 1 ? 0 + h.toString() : h;
    var m = min % 60;
    var minute = m.toString().length == 1 ? 0 + m.toString() : m;
    return hour + ':' + minute;
  }
  onSubmit(invalid) {
    if (invalid) {
      return;
    }
    this.fetchGuest();
  }

  onChangeWaitTime() {
    alert();
    console.log('' + this.waitTime);
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

      case 'DELETE':
        if (this.toggleColumnArr.deleteSym == true) {
          this.toggleColumnArr.deleteSym = false;
        } else {
          this.toggleColumnArr.deleteSym = true;
        }
        break;

      case 'SENDTEXT':
        if (this.toggleColumnArr.sendText == true) {
          this.toggleColumnArr.sendText = false;
        } else {
          this.toggleColumnArr.sendText = true;
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
