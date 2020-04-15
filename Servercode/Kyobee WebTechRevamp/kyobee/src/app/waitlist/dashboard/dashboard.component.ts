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
import { SendSMSDTO } from 'src/app/core/models/send-sms.model';

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
  sendSMSDTO: SendSMSDTO;
  organizationTemplateDTOList: Array<OrganizationTemplateDTO>;
  waitTime: any = null;
  content = null;
  level;
  totalGuest: number;
  totalPageNo: number;
  waitTimeOption = 100;
  smsContentDTO: SmsContentDTO = new SmsContentDTO();
  successMsg = null;
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
  results = true;

  ngOnInit() {
    this.user = this.authService.getUser();
    this.orgId = this.user.organizationID;
    this.pageNo = 0;
    this.pageSize = 10;
    this.searchText = null;
    this.pageNo = this.pageNo + 1;
    this.connectPubnub();
    this.fetchOrgMetrics();
    this.fetchGuest();
  }

  fetchGuest() {
    const params = new HttpParams()
      .set('orgId', this.orgId)
      .set('pageNo', this.pageNo)
      .set('pageSize', this.pageSize)
      .set('searchText', this.searchText);
    this.guestService.fetchGuestList(params).subscribe((res: any) => {
      if (res.success === 1) {
        this.guestDTOList = res.serviceResult.records;
        this.totalGuest = res.serviceResult.totalRecords;
        this.pageNo = res.serviceResult.pageNo;
        this.pagination(this.totalGuest, this.pageNo, this.pageSize);
        console.log('user==' + JSON.stringify(this.guestDTOList));
        if (res.serviceResult.records == [] || res.serviceResult.records == '') {
          this.results = false;
        } else {
          this.results = true;
        }
        this.selectedGuest = new GuestDTO();
      } else {
        alert(res.message);
      }
    });
  }

  fetchOrgMetrics() {
    const params = new HttpParams().set('orgId', this.orgId);
    this.organizationService.fetchOrganizationMetrics(params).subscribe((res: any) => {
      if (res.success === 1) {
        this.organizationMetrics = res.serviceResult;
        this.waitTime = res.serviceResult.perPartyWaitTime;

        console.log('org == ' + JSON.stringify(this.organizationMetrics));
      } else {
        alert(res.message);
      }
    });
  }

  saveOrgMetrics() {
    const params = new HttpParams()
      .set('orgId', this.orgId)
      .set('perPartyWaitTime', this.waitTime)
      .set('numberOfUsers', this.organizationMetrics.notifyUserCount.toString());
    this.organizationService.saveOrganizationMetrics(params).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('org == ' + JSON.stringify(res));
        this.organizationMetrics = res.serviceResult.waitlistMetrics;
        this.organizationMetrics.totalWaitTime = res.serviceResult.waitlistMetrics.totalWaitTime;
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
    if (status === 'DELETE') {
      $('#deleteModal').modal('hide');
    } else if (status === 'SEATMODAL') {
      $('#seatModal').modal('hide');
    } else if (status === 'SMSMODAL') {
      $('#smsModal').modal('hide');
    }
    this.selectedGuest = new GuestDTO();
  }

  changeStatus(status) {
    const params = new HttpParams()
      .set('guestId', this.selectedGuest.guestID.toString())
      .set('orgId', this.orgId)
      .set('status', status);

    this.guestService.updateGuestStatus(params).subscribe((res: any) => {
      if (res.success === 1) {
        if (status === 'DELETE') {
          $('#deleteModal').modal('hide');
        } else {
          $('#seatModal').modal('hide');
        }

        this.selectedGuest = new GuestDTO();
      } else {
        if (status === 'DELETE') {
          $('#deleteModal').modal('hide');
        } else {
          $('#seatModal').modal('hide');
        }

        this.selectedGuest = new GuestDTO();
      }
      this.successMsg = res.serviceResult;
    });
  }

  fetchSMSContent(guest) {
    this.smsContentDTO = new SmsContentDTO();
    this.selectedGuest = guest;

    this.smsContentDTO.orgId = this.selectedGuest.organizationID;
    this.smsContentDTO.guestId = this.selectedGuest.guestID;
    this.smsContentDTO.clientBase = 'admin';
    this.smsContentDTO.guestName = this.selectedGuest.name;
    this.smsContentDTO.guestRank = this.selectedGuest.rank;
    this.smsContentDTO.guestUuid = this.selectedGuest.uuid;
    this.smsContentDTO.langId = this.selectedGuest.languagePref.langId;
    //this.smsContentDTO.tempLevel = 1;
    console.log('sms ' + JSON.stringify(guest));
    console.log('sms content:' + JSON.stringify(this.smsContentDTO));
    this.organizationService.fetchSmsContent(this.smsContentDTO).subscribe((res: any) => {
      if (res.success === 1) {
        this.organizationTemplateDTOList = res.serviceResult;
        console.log(JSON.stringify(this.organizationTemplateDTOList));
        if (this.organizationTemplateDTOList.length > 0) {
          this.content = this.organizationTemplateDTOList[0].templateText;
        }
        $('#smsModal').modal('show');
      } else {
        alert(res.serviceResult);
      }
    });
  }
  fetchOrgLevelMsg(level) {
    switch (level) {
      case 1:
        this.content = this.organizationTemplateDTOList[0].templateText;
        this.level = 1;
        break;
      case 2:
        this.content = this.organizationTemplateDTOList[1].templateText;
        this.level = 2;
        break;
      case 3:
        this.content = this.organizationTemplateDTOList[2].templateText;
        this.level = 3;
        break;
    }
  }
  fetchNextGuest() {
    this.pageNo = this.pageNo + 1;
    this.fetchGuest();
  }

  fetchPrevGuest() {
    this.pageNo = this.pageNo - 1;
    this.fetchGuest();
  }

  fetchGuestByPageNo(pageNo) {
    this.pageNo = pageNo;
    this.fetchGuest();
  }

  convertMinstoMMHH(min) {
    const h = Math.floor(min / 60);
    const hour = h.toString().length === 1 ? 0 + h.toString() : h;
    const m = min % 60;
    const minute = m.toString().length === 1 ? 0 + m.toString() : m;
    return hour + ':' + minute;
  }
  onSubmit(invalid) {
    if (invalid) {
      return;
    }
    if (this.searchText.toString().trim() === '') {
      this.searchText = null;
    }

    this.fetchGuest();
  }

  onChangeWaitTime() {
    console.log('' + this.waitTime);
  }

  toggleColumns(status) {
    switch (status) {
      case 'ACTION':
        if (this.toggleColumnArr.action === true) {
          this.toggleColumnArr.action = false;
        } else {
          this.toggleColumnArr.action = true;
        }
        break;

      case 'RANK':
        if (this.toggleColumnArr.rank === true) {
          this.toggleColumnArr.rank = false;
        } else {
          this.toggleColumnArr.rank = true;
        }
        break;

      case 'NOOFPARTY':
        if (this.toggleColumnArr.noOfParty === true) {
          this.toggleColumnArr.noOfParty = false;
        } else {
          this.toggleColumnArr.noOfParty = true;
        }
        break;

      case 'NAME':
        if (this.toggleColumnArr.name === true) {
          this.toggleColumnArr.name = false;
        } else {
          this.toggleColumnArr.name = true;
        }
        break;

      case 'CHECKIN':
        if (this.toggleColumnArr.checkInTime === true) {
          this.toggleColumnArr.checkInTime = false;
        } else {
          this.toggleColumnArr.checkInTime = true;
        }
        break;

      case 'NOTE':
        if (this.toggleColumnArr.note === true) {
          this.toggleColumnArr.note = false;
        } else {
          this.toggleColumnArr.note = true;
        }
        break;

      case 'DELETE':
        if (this.toggleColumnArr.deleteSym === true) {
          this.toggleColumnArr.deleteSym = false;
        } else {
          this.toggleColumnArr.deleteSym = true;
        }
        break;

      case 'SENDTEXT':
        if (this.toggleColumnArr.sendText === true) {
          this.toggleColumnArr.sendText = false;
        } else {
          this.toggleColumnArr.sendText = true;
        }
        break;

      case 'LANGUAGEPREF':
        if (this.toggleColumnArr.languagePref === true) {
          this.toggleColumnArr.languagePref = false;
        } else {
          this.toggleColumnArr.languagePref = true;
        }
        break;

      case 'OPTIN':
        if (this.toggleColumnArr.optIn === true) {
          this.toggleColumnArr.optIn = false;
        } else {
          this.toggleColumnArr.optIn = true;
        }
        break;
    }
  }
  sendSMS() {
    this.sendSMSDTO = new SendSMSDTO();
    this.sendSMSDTO.guestId = this.selectedGuest.guestID;
    this.sendSMSDTO.orgId = this.selectedGuest.organizationID;
    this.sendSMSDTO.smsContent = this.content;
    this.sendSMSDTO.templateLevel = this.level;
    console.log('sendSMS ' + JSON.stringify(this.sendSMSDTO));
    this.guestService.sendSMS(this.sendSMSDTO).subscribe((res: any) => {
      if (res.success === 1) {
        console.log(JSON.stringify(res));
        $('#smsModal').modal('hide');
      } else {
        $('#smsModal').modal('hide');
        alert(res.serviceResult);
      }
    });
  }
  pagination(totalItems, currentPage, pageSize) {
    currentPage = currentPage || 1;
    this.totalPageNo = Math.ceil(totalItems / pageSize);
    if (this.totalPageNo === 0) {
      this.totalPageNo = 1;
    }
    console.log('total pages' + this.totalPageNo);
  }

  connectPubnub() {
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
            this.fetchOrgMetrics();
          }
        }
        if (
          msg.message.op === 'UPDATE' ||
          msg.message.op === 'ADD' ||
          msg.message.op === 'resetOrganizationPusher' ||
          msg.message.op === 'DELETE' ||
          msg.message.op === 'INCOMPLETE' ||
          msg.message.op === 'NOTPRESENT' ||
          msg.message.op === 'SEATED'
        ) {
          if (msg.message.orgId === this.orgId) {
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
