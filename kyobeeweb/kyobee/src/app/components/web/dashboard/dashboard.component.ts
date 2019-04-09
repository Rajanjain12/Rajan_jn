import { Component, OnInit } from '@angular/core';
import { HttpService } from 'src/app/services/http.service';
import * as Realtime from 'realtime-messaging';

import { MainService } from 'src/app/services/main.service.js';

import imgLinks from '../../../../assets/data/imgLinks.json';
import globals from '../../../../assets/data/globals.json';

@Component({
	selector: 'app-dashboard',
	templateUrl: './dashboard.component.html',
	styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

	constructor(private httpService: HttpService, private mainService:MainService) { }

	public dashboardIlluImageSrc: string = imgLinks.dashboardIlluImg;
	public noteIconImg: string = imgLinks.notPresentImg;
	public deleteIconImg: string = imgLinks.deleteIconImg;
	public msgIconImg: string = imgLinks.msgIconImg;
	public notPresentImg: string = imgLinks.notPresentImg;
	public loading: boolean = true;
	public errorMsg = null;
	public successMsg = null;
	public appKey = null;
	public privateKey = null;
	public channel = null;
	public client = null;
	public countMsgChannel = 0;
	public totalWaitTime = null;
	public pager;
	public pagerRequest = null;
	public pageSize = 100;
	public userDTO = null; //-------------------------------
	public guestWaitList = null;
	public userCount = null;
	public waitTime = null;
	public notifyFirst = null;

	/* 
	$scope.wiatTimeOption = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99]; // change by sunny (25-07-2018)
	
	$scope.notifyOption = [1,2,3,4,5,6,7,8,9,10]; // change by sunny (25-07-2018)
	$scope.selectedGuest = null; */

	/* $scope.textSent = true;
	$scope.smsContents=null;
	$scope.smsContent=null;
	$scope.smsContentLevel1 = null;
					$scope.smsContentLevel2 = null;
					$scope.smsContentLevel3 = null;
					$scope.activeId=null;
					
					$scope.searchName = null;
					$scope.pageSize = 100;
					
					$scope.showHistory = false;
					
					$scope.toggleColumnShowHide = false;
					
					$scope.statusOptions=["All","Not Present","Incomplete"];
					$scope.selectedStatus=$scope.statusOptions[0];
					$scope.countMessage1 = 'Max Character limit 225';
					$scope.countMessage2 = 'Max Character limit 225';
					$scope.countMessage3 = 'Max Character limit 225'; */

	ngOnInit() {
		this.loadInfo();
		this.mainService.fetchUserDetails();
		//this.loadWaitListPage(1);
	}

	loadInfo(){
		this.loading = true;
		this.errorMsg = null;
		var params = {};
		var url = '/kyobee/web/rest/waitlistRestAction/pusgerinformation';

		this.httpService.getDataService(url, params).subscribe(data => {
			alert(JSON.stringify(data));
			if (data.status == "SUCCESS") {
				this.appKey = data.serviceResult.REALTIME_APPLICATION_KEY;
				this.privateKey = data.serviceResult.REALTIME_PRIVATE_KEY;
				this.channel = data.serviceResult.pusherChannelEnv;
				this.loadFactory();
				this.loading = false;
			} else if (data.status == "FAILURE") {
				this.errorMsg = "Error while fetching user details. Please login again or contact support";
				this.loading = false;
			}
		},
			error => {
				this.errorMsg = "Error while fetching user details. Please login again or contact support";
				this.loading = false;
			});
	}

	loadFactory() {
		this.client = Realtime.createClient();
		this.client.setClusterUrl(globals["realtime-connectionUrl"]);
		this.client.setConnectionMetadata(globals["realtime-connectionMetadata"]);
		this.client.connect(this.appKey, globals["realtime-authToken"]);
		this.client.onConnected = (client) => {
			console.log("realtime connected");
			console.log('Connected to: ' + client.getUrl());
			alert('Connected to: ' + client.getUrl());
			console.log('Subscribe to channel: ' + this.channel);
			alert('Subscribe to channel: ' + this.channel);

			if (this.client.getIsConnected() == false) {
				this.client.connect(this.appKey, globals["realtime-authToken"]);
			}
			
			client.subscribe(this.channel, true, (client, channel, message) => { //channel to be replaced
					console.log("Received message:", message);
					alert("Received message:"+ message);
					this.countMsgChannel++;
					this.countMsgChannel++;
					console.log('Received (' + this.countMsgChannel + '): ' + message + ' at channel: ' + channel);
					var m = JSON.parse(message);
					alert("m "+m);
					if (m.orgid == this.userDTO.organizationId) {
					this.totalWaitTime = m.totalWaitTime;
					if (m.OP != "NOTIFY_USER") { this.loadWaitListPage(1); }
				}
			});
		}
		this.client.onSubscribed = clientSubscribed;
		this.client.onUnsubscribed = clientUnsubscribed;
		this.client.onReconnecting = clientReconnecting;
		this.client.onReconnected = clientReconnected;
		this.client.onDisconnected = clientDisconnected;
		this.client.onException = clientException;
		
		function clientSubscribed(ortc, channel) {
			console.log('Subscribed to channel: ' + channel);
		};
		function clientUnsubscribed(ortc, channel) {
			console.log('Unsubscribed from channel: ' + channel);
		};
		function clientReconnecting(ortc) {
			console.log('Reconnecting to ' + this.connectionUrl);
		};
		function clientReconnected(ortc) {
			console.log('Reconnected to: ' + ortc.getUrl());
		};
		function clientDisconnected(ortc) {
			console.log('Disconnected', 'disconnected');
		};
		function clientException(ortc, error) {
			console.log('Error: ' + error);
		};
	}

	loadWaitListPage(pageNo) {
		console.log(pageNo);
		/* if (pageNo < 1 || pageNo > this.pager.totalPages) {
			return;
		} */
		this.loadWaitListGuests(pageNo);
	}

	loadWaitListGuests(pageNo) {

		this.pagerRequest = {
			filters: null,
			sort: null,
			sortOrder: null,
			pageSize: this.pageSize,
			pageNo: pageNo
		}

		var postBody = {
			orgid: this.userDTO.organizationId,
			partyType: "C",
			pagerReqParam: this.pagerRequest
		};
		console.log(JSON.stringify(postBody));
		this.loadOrgMetricks();
		var url = '/kyobee/web/rest/waitlistRestAction/checkinusers';

		this.httpService.getDataService(url, '').subscribe(data => {
			alert(JSON.stringify(data));
			if (data.status == "SUCCESS") {
				var paginatedResponse = data.serviceResult;
				this.guestWaitList = paginatedResponse.records;
				//this.pager = 	KyobeeService.getPager(paginatedResponse.totalRecords, pageNo, $scope.pageSize);
				this.userCount = paginatedResponse.totalRecords;
				console.log(this.pager);
			} else if (data.status == null) {
				this.guestWaitList = null;
				this.pager = null;
			} else if (data.status == "FAILURE") {
				this.errorMsg = "Error while fetching GuestList";
				this.loading = false;
			}
		},
			error => {
				this.errorMsg = "Error while fetching GuestList.. Please login again or contact support";
				this.loading = false;
			});
	};

	loadOrgMetricks() {
		var postBody = {};
		var url = '/kyobee/web/rest/waitlistRestAction/totalwaittimemetricks?orgid=' + this.userDTO.organizationId;

		this.httpService.getDataService(url, '').subscribe(data => {
			alert(JSON.stringify(data));
			if (data.status == "SUCCESS") {
				this.waitTime = data.serviceResult.ORG_WAIT_TIME;
				this.notifyFirst = data.serviceResult.OP_NOTIFYUSERCOUNT;
				this.totalWaitTime = data.serviceResult.ORG_TOTAL_WAIT_TIME;
			} else if (data.status == "FAILURE") {
				this.errorMsg = "Error while fetching wait times";
				this.loading = false;
			}
		},
			error => {
				this.errorMsg = "Error while fetching wait times.. Please login again or contact support";
				this.loading = false;
			});
	};
}
