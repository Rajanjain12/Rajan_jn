
KyobeeControllers.controller('waitListCtrl',
		[
				'$scope',
				'$rootScope',
				'$location',
				'$timeout',
				'$interval',
				'$routeParams',
				'$q',
				'KyobeeService',
				function($scope, $rootScope,$location, $timeout, $interval, $routeParams, $q , KyobeeService) {
						
					/*$scope.sliderStartTime = 'hello';*/
					$rootScope.hideHeader=false;//To hide show header in index.html
					$scope.waitTime = null;
					$scope.wiatTimeOption = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99]; // change by sunny (25-07-2018)
					$scope.notifyFirst = null;
					$scope.notifyOption = [1,2,3,4,5,6,7,8,9,10]; // change by sunny (25-07-2018)
					$scope.totalWaitTime = null;
					$scope.guestWaitList = null;
					$scope.totalGuestWaitList = null;
					$scope.selectedGuest = null;
					$scope.client = null;
					$scope.countMsgChannel = 0;
					$scope.textSent = true;
					$scope.smsContents=null;
					$scope.smsContent=null;
					$scope.smsContentLevel1 = null;
					$scope.smsContentLevel2 = null;
					$scope.smsContentLevel3 = null;
					$scope.activeId=null;
					
					$scope.loading = false; /* for loader(krupali 07/07/2017)*/
					$scope.searchName = null;
					$scope.appKey = null;
		            $scope.privateKey = null;
		            $scope.channel = null;
					
					$scope.pager = {};
					$scope.pagerRequest = null;
					$scope.pageSize = 100;
					
					$scope.showHistory = false;
					
					$scope.toggleColumnShowHide = false;
					
					$scope.successMsg = null;
					/*$scope.clientBase = $scope.userDTO.clientBase;
					alert($scope.clientBase);*/
					
					$scope.statusOptions=["All","Not Present","Incomplete"];
					$scope.selectedStatus=$scope.statusOptions[0];
					
					$scope.userCount = null;  /*for solving footer issue (krupali 17/07/2017)*/
					$scope.countMessage1 = 'Max Character limit 225';
					$scope.countMessage2 = 'Max Character limit 225';
					$scope.countMessage3 = 'Max Character limit 225';
					
					$scope.hideSuccessMsg = function(){
						$scope.successMsg = null;
					};
					
					$scope.toggleColumn = {
							seat : true,
							no : true,
							name : true,
							seatingPref : true,
							noOfParty : true,
							checkinTime : true,
							partyType : true,
							quoteTime : true,
							optIn : false,
							note : true,
							del : true,
							partyRank : true,
							sendText : true,
							langPref : false
					};
					
					/*$scope.offset = new date().getTimezoneOffset();*/
					/*alert($scope.offset);*/
					$scope.slider = {
							id : 'timeSlider',
							range:{
								min:00,
								max:24
							},
							minTime:9,
							maxTime:15,
							/*showValues: "true",*/
							
					};
			
					
					$scope.showToggleColumn = function(){
						if($scope.toggleColumnShowHide){
							$scope.toggleColumnShowHide = false;
							
						}else {
							$scope.toggleColumnShowHide = true;
						}						
					}
					
					$scope.toggleShowHistory = function(){
						if($scope.showHistory){
							$scope.showHistory = false;
							$scope.pager = {};
							// load normal waitlist
							$scope.loadWaitListPage(1);
						}else {
							$scope.showHistory = true;
							$scope.selectedStatus=$scope.statusOptions[0];
							$scope.loadHistoryPage(1);
							// Load History
						}						
					}
		            
		            $scope.loadOrgMetricks = function() {
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/totalwaittimemetricks?orgid=' + $scope.userDTO.organizationId;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.waitTime = data.serviceResult.ORG_WAIT_TIME;
										$scope.notifyFirst = data.serviceResult.OP_NOTIFYUSERCOUNT;
										$scope.totalWaitTime = data.serviceResult.ORG_TOTAL_WAIT_TIME;
									} else if (data.status == "FAILURE") {
										alert('Error while fetching wait times.');
									}
								}, function(error) {
									alert('Error while fetching wait times.. Please login again or contact support');
								});
					};
					
					$scope.loadInfo = function() {
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/pusgerinformation';
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log("----"+data);
									if (data.status == "SUCCESS") {
										$scope.appKey = data.serviceResult.REALTIME_APPLICATION_KEY;
										$scope.privateKey = data.serviceResult.REALTIME_PRIVATE_KEY;
										$scope.channel = data.serviceResult.pusherChannelEnv;
										$scope.loadFactory();
									} else if (data.status == "FAILURE") {
										alert('Error while fetching user details. Please login again or contact support');
										$scope.logout();
									}
								}, function(error) {
									alert('Error while fetching user details. Please login again or contact support');
								});
					};
					
					$scope.toggleColumns = function(status){
						/*alert("in toggleColumns")*/
						console.log(status);
						
						switch (status) {
						case 'seat':
							if($scope.toggleColumn.seat == true){
								$scope.toggleColumn.seat = false;
							}
							else{
								$scope.toggleColumn.seat = true;
							}
							break;
						
						case 'no':
							if($scope.toggleColumn.no == true){
								$scope.toggleColumn.no = false; 
							}
							else{
								$scope.toggleColumn.no = true;
							}
							break;
							
						case 'name':
							if($scope.toggleColumn.name == true){
								$scope.toggleColumn.name = false; 
							}
							else{
								$scope.toggleColumn.name = true;
							}
							break;
							
						case 'seatingPref':
							if($scope.toggleColumn.seatingPref == true){
								$scope.toggleColumn.seatingPref = false; 
							}
							else{
								$scope.toggleColumn.seatingPref = true;
							}
							break;
							
						case 'noOfParty':
							if($scope.toggleColumn.noOfParty == true){
								$scope.toggleColumn.noOfParty = false; 
							}
							else{
								$scope.toggleColumn.noOfParty = true;
							}
							break;
							
						case 'checkinTime':
							if($scope.toggleColumn.checkinTime == true){
								$scope.toggleColumn.checkinTime = false; 
							}
							else{
								$scope.toggleColumn.checkinTime = true;
							}
							break;
							
						case 'partyType':
							if($scope.toggleColumn.partyType == true){
								$scope.toggleColumn.partyType = false; 
							}
							else{
								$scope.toggleColumn.partyType = true;
							}
							break;
						
						case 'quoteTime':
							if($scope.toggleColumn.quoteTime == true){
								$scope.toggleColumn.quoteTime = false; 
							}
							else{
								$scope.toggleColumn.quoteTime= true;
							}
							break;
						case 'optIn':
							if($scope.toggleColumn.optIn == true){
								$scope.toggleColumn.optIn = false; 
							}
							else{
								$scope.toggleColumn.optIn = true;
							}
							break;
							
						case 'note':
							if($scope.toggleColumn.note == true){
								$scope.toggleColumn.note = false; 
							}
							else{
								$scope.toggleColumn.note = true;
							}
							break;
						
						case 'del':
							if($scope.toggleColumn.del == true){
								$scope.toggleColumn.del = false; 
							}
							else{
								$scope.toggleColumn.del = true;
							}
							break;
							
						case 'partyRank':
							if($scope.toggleColumn.partyRank == true){
								$scope.toggleColumn.partyRank = false; 
							}
							else{
								$scope.toggleColumn.partyRank = true;
							}
							break;
						
						case 'sendText':
							if($scope.toggleColumn.sendText == true){
								$scope.toggleColumn.sendText = false; 
							}
							else{
								$scope.toggleColumn.sendText = true;
							}
							break;
						
						case 'langPref':
							if($scope.toggleColumn.langPref == true){
								$scope.toggleColumn.langPref = false; 
							}
							else{
								$scope.toggleColumn.langPref = true;
							}
							break;
							
						default:
							break;
						}
					}
					
					$scope.loadWaitListPage = function(pageNo){						
						console.log("page number --"+pageNo);
						if (pageNo < 1 || pageNo > $scope.pager.totalPages) {
				            return;
				        }
						
						$scope.loadWaitListGuests(pageNo);
						
					}
					 
					$scope.loadWaitListGuests = function(pageNo) {
						
						$scope.pagerRequest = {
								filters : null,
								sort : null,
								sortOrder: null,
								pageSize : $scope.pageSize,
								pageNo : pageNo
						}
						
						var postBody = {
								orgid : $scope.userDTO.organizationId,
								partyType : "C",
								pagerReqParam : $scope.pagerRequest								
						};
						console.log(JSON.stringify(postBody));
						$scope.loadOrgMetricks();
						var url = '/kyobee/web/rest/waitlistRestAction/checkinusers';
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log("Postbody "+JSON.stringify(postBody));
									console.log("Waitlist Grid data : "+ JSON.stringify(data));
									if (data.status == "SUCCESS") {
										var paginatedResponse = data.serviceResult;
										//$scope.guestWaitList = paginatedResponse.records;
										$scope.totalGuestWaitList = paginatedResponse.records;
										$scope.pager = 	KyobeeService.getPager(paginatedResponse.totalRecords, pageNo,$scope.pageSize);
										$scope.guestWaitList=$scope.totalGuestWaitList.slice($scope.pager.startIndex, $scope.pager.endIndex + 1);
										$scope.userCount = paginatedResponse.totalRecords;
										console.log($scope.pager);
									}else if(data.status == null) {
										$scope.totalGuestWaitList=null;
										$scope.guestWaitList = null;
										$scope.pager = {};
									}else if (data.status == "FAILURE") {
										alert('Error while fetching wait times.');
									}
								}, function(error) {
									alert('Error while fetching wait times.. Please login again or contact support');
								});
					};
					
					var toUTCDate = function(date){
				        var _utc = new Date(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate(),  date.getUTCHours(), date.getUTCMinutes(), date.getUTCSeconds());
				        return _utc;
				      };
				      
				      var millisToUTCDate = function(millis){       
				       if(millis==null)
				        return null;
				       return toUTCDate(new Date(millis));
				      };
				      
				        $scope.toUTCDate = toUTCDate;
				        $scope.millisToUTCDate = millisToUTCDate;
					
					
					$scope.searchGrid = function(searchName) {
						if(searchName==null)
						{
							$scope.loadWaitListPage(1);
						 }
						else if(searchName.trim()==''){
							$scope.loadWaitListPage(1);
						}
						else
						{
							$scope.pagerRequest = {
									filters : null,
									sort : null,
									sortOrder: null,
									pageSize : $scope.pageSize,
									pageNo : 1
							}
							
							var postBody = {
									orgid : $scope.userDTO.organizationId,
									partyType : "C",
									searchName : $scope.searchName,
									pagerReqParam : $scope.pagerRequest								
							};
							
							$scope.loadOrgMetricks();
							
							var url = '/kyobee/web/rest/waitlistRestAction/searchuser';
							KyobeeService.getDataService(url, '').query(postBody,
									function(data) {
										console.log("Waitlist Grid data : "+ JSON.stringify(data));
										if (data.status == "SUCCESS") {
											var paginatedResponse = data.serviceResult;
											$scope.totalGuestWaitList = paginatedResponse.records;
											$scope.pager = 	KyobeeService.getPager(paginatedResponse.totalRecords, paginatedResponse.pageNo, $scope.pageSize);
											$scope.guestWaitList=$scope.totalGuestWaitList.slice($scope.pager.startIndex, $scope.pager.endIndex + 1);
											$scope.userCount = paginatedResponse.totalRecords;
											console.log($scope.pager);
										}else if(data.status == null) {
											$scope.totalGuestWaitList=null;
											$scope.guestWaitList = null;
											$scope.pager = {};
										}else if (data.status == "FAILURE") {
											alert('Error while fetching wait times.');
										}
									}, function(error) {
										alert('Error while fetching wait times.. Please login again or contact support');
									});
						
						}
					
					};
					
					$scope.loadHistoryPage = function(pageNo) {
						debugger;
						$scope.pagerRequest = {
								filters : null,
								sort : null,
								sortOrder: null,
								pageSize : $scope.pageSize,
								pageNo : pageNo
						}
						
						/*current timezone (krupali 06/07/2017)*/
						function pad(value) {
						    return value < 10 ? '0' + value : value;
						}
						
						function createOffset(date) {
						    var sign = (date.getTimezoneOffset() > 0) ? "-" : "+";
						    var offset = Math.abs(date.getTimezoneOffset());
						    var hours = pad(Math.floor(offset / 60));
						    var minutes = pad(offset % 60);
						    return sign + hours + ":" + minutes;
						}
						
						var d = new Date(); 
						tzName = createOffset(d);
						/*alert(tzName);*/
						
						var postBody = {
								orgid : $scope.userDTO.organizationId,
								statusOption: $scope.selectedStatus,
								pagerReqParam : $scope.pagerRequest,
								sliderMinTime : $scope.slider.minTime,
								sliderMaxTime : $scope.slider.maxTime,
								clientTimezone : tzName
						};
						
						//$scope.loadOrgMetricks();
						var url = '/kyobee/web/rest/waitlistRestAction/history';
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										var paginatedResponse = data.serviceResult;
										console.log("response "+JSON.stringify(data));
										$scope.guestWaitList = paginatedResponse.records;
										$scope.userCount = paginatedResponse.totalRecords;
										$scope.pager = 	KyobeeService.getPager(paginatedResponse.totalRecords, pageNo, $scope.pageSize);
										console.log($scope.pager);
									} else if (data.status == "FAILURE") {
										alert('Error while fetching history');
									}
								}, function(error) {
									alert('Error while fetching wait times.. Please login again or contact support');
								});
					};
					
					
					$scope.searchGridOnHistory = function(searchName) {
						function pad(value) {
						    return value < 10 ? '0' + value : value;
						}
						
						function createOffset(date) {
						    var sign = (date.getTimezoneOffset() > 0) ? "-" : "+";
						    var offset = Math.abs(date.getTimezoneOffset());
						    var hours = pad(Math.floor(offset / 60));
						    var minutes = pad(offset % 60);
						    return sign + hours + ":" + minutes;
						}
						
						var d = new Date(); 
						tzName = createOffset(d);
						/*alert(tzName);*/
						
						$scope.pagerRequest = {
								filters : null,
								sort : null,
								sortOrder: null,
								pageSize : $scope.pageSize,
								pageNo : 1
						}
						
						console.log($scope.selectedStatus);
						

						
						var postBody = {
								orgid : $scope.userDTO.organizationId,
								statusOption: $scope.selectedStatus,
								pagerReqParam : $scope.pagerRequest,
								searchName : $scope.searchName,
								sliderMinTime : $scope.slider.minTime,
								sliderMaxTime : $scope.slider.maxTime,
								clientTimezone : tzName
						};
						
						//$scope.loadOrgMetricks();
						var url = '/kyobee/web/rest/waitlistRestAction/searchhistoryuser';
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										var paginatedResponse = data.serviceResult;
										$scope.guestWaitList = paginatedResponse.records;
										$scope.userCount = paginatedResponse.totalRecords;
										$scope.pager = 	KyobeeService.getPager(paginatedResponse.totalRecords, pageNo, $scope.pageSize);
										console.log($scope.pager);
									} else if (data.status == "FAILURE") {
										alert('Error while fetching history');
									}
								}, function(error) {
									alert('Error while fetching wait times.. Please login again or contact support');
								});
					};
					/*$scope.searchGridOnHistory = function(searchName) {
						alert("in searching");
						
						$scope.pagerRequest = {
								filters : null,
								sort : null,
								sortOrder: null,
								pageSize : $scope.pageSize,
								pageNo : 1
						}
						
						var postBody = {
								orgid : $scope.userDTO.organizationId,
								partyType : "C",
								searchName : $scope.searchName,
								pagerReqParam : $scope.pagerRequest								
						};
						alert("after postbody");
						$scope.loadOrgMetricks();
						alert("after loading");
						var url = '/kyobee/web/rest/waitlistRestAction/searchuserhistory';
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log("Postbody "+JSON.stringify(postBody));
									console.log("Waitlist Grid data : "+ JSON.stringify(data));
									if (data.status == "SUCCESS") {
										alert("its success");
										var paginatedResponse = data.serviceResult;
										$scope.guestWaitList = paginatedResponse.records;
										alert("guestWaitList" + JSON.stringify(paginatedResponse));
										alert("after variable");
										$scope.pager = 	KyobeeService.getPager(paginatedResponse.totalRecords, paginatedResponse.pageNo, $scope.pageSize);
										alert(paginatedResponse.totalRecords);
										alert(pageNo);
										alert($scope.pageSize);
										alert("record displayed");
										console.log($scope.pager);
									}else if(data.status == null) {
										$scope.guestWaitList = null;
										$scope.pager = {};
									}else if (data.status == "FAILURE") {
										alert('Error while fetching wait times.');
									}
								}, function(error) {
									alert('Error while fetching wait times.. Please login again or contact support');
								});
					};*/
					
					$scope.showPopup = function(guestObj){
						
						$('#showpopup').simplePopup();
						$scope.scrollToTop();
						$scope.selectedGuest = guestObj;
					}
					
					$scope.showDeletePopup = function(guestObj){
						$('#deletePopup').simplePopup();
						$scope.scrollToTop();
						$scope.selectedGuest = guestObj;
					}
					
					$scope.fetchSmsContent = function(guestObj,level){
						var defered=$q.defer();
						$scope.smsContentParam = {
								orgId : $scope.userDTO.organizationId,
								guestId : guestObj.guestID,
								guestName : guestObj.name,
								langId : guestObj.languagePref.langId,
								tempLevel : level,
								gusetRank : guestObj.rank,
								clientBase : $scope.userDTO.clientBase,
								guestUuid : guestObj.uuid
						}
						
						var postBody = $scope.smsContentParam;
						console.log("sendSMS postbody-----"+JSON.stringify(postBody));
						var url = '/kyobee/web/rest/waitlistRestAction/fetchSmsContent' ;
						KyobeeService.postDataService(url,'').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.smsContents = data.serviceResult;
										$scope.smsContentLevel1 = $scope.smsContents.smsLevel1;
										$scope.smsContentLevel2 = $scope.smsContents.smsLevel2;
										$scope.smsContentLevel3 = $scope.smsContents.smsLevel3;
										defered.resolve();
									} else if (data.status == "FAILURE") {
										$scope.loading=false;
										alert('Error while fetching sms content');
									}
								}, function(error) {
									defered.reject();
									$scope.loading=false;
									alert('Error while fetching sms content');
								});
						return defered.promise;
					}
					
					$scope.showSendSMSPopup = function(guestObj){
						$scope.loading=true;
						$scope.countMessage1 = 'Max Character limit 225';
						$scope.countMessage2 = 'Max Character limit 225';
						$scope.countMessage3 = 'Max Character limit 225';
						$scope.errorMessage = null;
						$scope.selectedGuest = guestObj;
						var promise=$scope.fetchSmsContent(guestObj,3);
						promise.then(function(){
							debugger;
							$scope.scrollToTop();
							$('#sendSMSPopup').simplePopup();
							$scope.loading=false;
						},function(error){
							
						});
						
					}
					
					/*$scope.showSendSMSPopup = function(guestObj){
						$scope.countMessage = 'Max Character limit 150';
						$scope.errorMessage = null;
						$('#sendSMSPopup').simplePopup();
						$scope.selectedGuest = guestObj;
						//$scope.smsContent = 'this is default message from admin';
						console.log("------------"+JSON.stringify($scope.selectedGuest));
						
						//for dev(ordextechnology.com)
						switch ($scope.userDTO.clientBase) {
						case "admin":
							if($scope.selectedGuest.languagePref.langId==134){
								$scope.smsContent = '顧客 '+$scope.selectedGuest.rank+' : 您的桌位 即將 準備就緒。請帶您的全部客人回到餐廳以等待叫到您的號碼。點擊鏈接查詢實時排隊信息: https://tinyurl.com/y7zv5wax/s/'+$scope.selectedGuest.uuid;
							}else{
								$scope.smsContent = 'Guest '+$scope.selectedGuest.rank+' : Table is almost ready. Come back and wait for your name to be called. For updates: https://tinyurl.com/y7zv5wax/s/'+$scope.selectedGuest.uuid;
							}
							break;
						case "advantech":
							if($scope.selectedGuest.languagePref.langId==134){
								$scope.smsContent = '顧客 '+$scope.selectedGuest.rank+' : 您的桌位 即將 準備就緒。請帶您的全部客人回到餐廳以等待叫到您的號碼。點擊鏈接查詢實時排隊信息: https://tinyurl.com/y7nl9ugt/s/'+$scope.selectedGuest.uuid;
							}else{

								$scope.smsContent = 'Guest '+$scope.selectedGuest.rank+' : Table is almost ready. Come back and wait for your name to be called. For updates: https://tinyurl.com/y7nl9ugt/s/'+$scope.selectedGuest.uuid;
							}
							break;
						case "sweethoneydessert":
							if($scope.selectedGuest.languagePref.langId==134){
								$scope.smsContent = '顧客 '+$scope.selectedGuest.rank+' : 您的桌位 即將 準備就緒。請帶您的全部客人回到餐廳以等待叫到您的號碼。點擊鏈接查詢實時排隊信息: https://tinyurl.com/y95p4eug/s/'+$scope.selectedGuest.uuid;
							}else{
								$scope.smsContent = 'Guest '+$scope.selectedGuest.rank+' : Table is almost ready. Come back and wait for your name to be called. For updates: https://tinyurl.com/y95p4eug/s/'+$scope.selectedGuest.uuid;
							}
							break;
						default:
							if($scope.selectedGuest.languagePref.langId==134){
								$scope.smsContent = '顧客 '+$scope.selectedGuest.rank+' : 您的桌位 即將 準備就緒。請帶您的全部客人回到餐廳以等待叫到您的號碼。點擊鏈接查詢實時排隊信息: https://tinyurl.com/y7zv5wax/s/'+$scope.selectedGuest.uuid;
							}else{
								$scope.smsContent = 'Guest '+$scope.selectedGuest.rank+' : Table is almost ready. Come back and wait for your name to be called. For updates: https://tinyurl.com/y7zv5wax/s/'+$scope.selectedGuest.uuid;
							}
							break;
						}
						
						//for prod(kyobee.com)
						switch ($scope.userDTO.clientBase) {
						case "admin":
							if($scope.selectedGuest.languagePref.langId==134){
								$scope.smsContent = '顧客 '+$scope.selectedGuest.rank+' : 您的桌位 即將 準備就緒。請帶您的全部客人回到餐廳以等待叫到您的號碼。點擊鏈接查詢實時排隊信息: https://tinyurl.com/yc8kfl5m/s/'+$scope.selectedGuest.uuid;
							}else{
								$scope.smsContent = 'Guest '+$scope.selectedGuest.rank+' : Table is almost ready. Come back and wait for your name to be called. For updates: https://tinyurl.com/yc8kfl5m/s/'+$scope.selectedGuest.uuid;
							}
							break;
						case "advantech":
							if($scope.selectedGuest.languagePref.langId==134){
								$scope.smsContent = '顧客 '+$scope.selectedGuest.rank+' : 您的桌位 即將 準備就緒。請帶您的全部客人回到餐廳以等待叫到您的號碼。點擊鏈接查詢實時排隊信息: https://tinyurl.com/y88vbqjx/s/'+$scope.selectedGuest.uuid;
							}else{

								$scope.smsContent = 'Guest '+$scope.selectedGuest.rank+' : Table is almost ready. Come back and wait for your name to be called. For updates: https://tinyurl.com/y88vbqjx/s/'+$scope.selectedGuest.uuid;
							}
							break;
						case "sweethoneydessert":
							if($scope.selectedGuest.languagePref.langId==134){
								$scope.smsContent = '顧客 '+$scope.selectedGuest.rank+' : 提示 ... ... 已叫到您的號碼。請立即返回杏記甜品，前臺會重新安排您的桌位。謝謝，杏記甜品 Colma';
							}else{
								$scope.smsContent = 'Guest '+$scope.selectedGuest.rank+' : Alert…you’ve been called. Please return to the store IMMEDIATELY. See FRONT DESK for re-arrangement. Best regards, SweethoneyDessert Colma';
							}
							break;
						default:
							if($scope.selectedGuest.languagePref.langId==134){
								$scope.smsContent = '顧客 '+$scope.selectedGuest.rank+' : 您的桌位 即將 準備就緒。請帶您的全部客人回到餐廳以等待叫到您的號碼。點擊鏈接查詢實時排隊信息: https://tinyurl.com/yc8kfl5m/s/'+$scope.selectedGuest.uuid;
							}else{
								$scope.smsContent = 'Guest '+$scope.selectedGuest.rank+' : Table is almost ready. Come back and wait for your name to be called. For updates: https://tinyurl.com/yc8kfl5m/s/'+$scope.selectedGuest.uuid;
							}
							break;
						}
						
					}*/
					
					$scope.incrementCalloutCount = function(){
						$scope.loading=true;													/* for loader(krupali 07/07/2017)*/
						$scope.successMsg = null;
						var postBody = $scope.selectedGuest;
						
						var url = '/kyobee/web/rest/waitlistRestAction/incrementCalloutCount';
						KyobeeService.postDataService(url, '').query(postBody,
								function(data) {
									console.log(data);				
									if (data.status == "SUCCESS") {
										$scope.loading = false;									/* for loader(krupali 07/07/2017)*/
										console.log("scccessfully called out.");
										console.log(data.serviceResult);
										$('#showpopup').simplePopup().hide();
										$(".simplePopupBackground").fadeOut("fast");
										if ($scope.client.getIsConnected() == false) {
											$scope.client.connect($scope.appKey, $scope.authToken);
						                }
						                var message = JSON.stringify({
						                    "OP": "NOT_PRESENT",
						                    "guestObj": data.guest,
						                    "FROM": "ADMIN",
						                    "ppwt": $scope.waitTime,
						                    "orgid": $scope.userDTO.organizationId
						                });
						                $scope.client.send($scope.channel, message);
						                console.log('Sending from not present: ' + message + ' to channel: ' + $scope.channel);
									} else if (data.status == "FAILURE") {
										alert('Error while updating callout for guest');
									}
								}, function(error) {
									alert('Error while updating callout for guest');
								});
					}
					
					$scope.markasIncomplete = function(){
						$scope.loading=true;											/* for loader(krupali 07/07/2017)*/
						$scope.successMsg = null;
						var postBody = $scope.selectedGuest;
						
						var url = '/kyobee/web/rest/waitlistRestAction/markAsIncomplete';
						KyobeeService.postDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.loading=false;							/* for loader(krupali 07/07/2017)*/
										console.log("scccessfully called out.");
										console.log(data.serviceResult);
										$('#showpopup').simplePopup().hide();
										$(".simplePopupBackground").fadeOut("fast");
										if ($scope.client.getIsConnected() == false) {
											$scope.client.connect($scope.appKey, $scope.authToken);
						                }
						                var message = JSON.stringify({
						                    "OP": "INCOMPLETE",
						                    "guestObj": data.guest,
						                    "FROM": "ADMIN",
						                    "ppwt": $scope.waitTime,
						                    "orgid": $scope.userDTO.organizationId
						                });
						                $scope.client.send($scope.channel, message);
						                console.log('Sending from mark as incomplete: ' + message + ' to channel: ' + $scope.channel);
									} else if (data.status == "FAILURE") {
										alert('Error while updating callout for guest');
									}
								}, function(error) {
									alert('Error while updating callout for guest');
								});	
						
					} 
					
					$scope.markasSeated = function(){
						$scope.loading = true;										/* for loader(krupali 07/07/2017)*/
						$scope.successMsg = null;
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/markAsSeated?orgId=' + $scope.userDTO.organizationId + '&guestId='+$scope.selectedGuest.guestID;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.loading=false;						/* for loader(krupali 07/07/2017)*/
										$('#showpopup').simplePopup().hide();
										$(".simplePopupBackground").fadeOut("fast");
										$scope.loadWaitListPage(1);
										console.log(data);
									} else if (data.status == "FAILURE") {
										alert('Error while marking as seated.');
									}
								}, function(error) {
									alert('Error while marking as seated');
								});
						
					}
					
					$scope.deleteGuest = function(){
						$scope.successMsg = null;
						$scope.loading = true;
						var postBody = $scope.selectedGuest;
						
						var url = '/kyobee/web/rest/waitlistRestAction/deleteGuest';
						KyobeeService.postDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$('#deletePopup').simplePopup().hide();
										$(".simplePopupBackground").fadeOut("fast");
										$scope.loadWaitListPage(1);
										$scope.loading = false;
										$scope.successMsg = "Guest Deleted Successfully."
									} else if (data.status == "FAILURE") {
										$scope.loading = false;
										alert('Error while deleting guest.');
									}
									$scope.loading = false;
								}, function(error) {
									$scope.loading = false;
									alert('Error while deleting guest.');
								});
						
					}
					
					$scope.sendSMS = function(activeId){
						if(activeId == null){
							activeId = 1;
						}
						switch (activeId) {
						case 1:
							$scope.smsContent = $scope.smsContentLevel1;
							break;
							
						case 2:
							$scope.smsContent = $scope.smsContentLevel2;
							break;
							
						case 3:
							$scope.smsContent = $scope.smsContentLevel3;
							break;

						default:
							break;
						}
						
						if ($scope.smsContent == null || $scope.smsContent == '') {
							$scope.errorMessage = 'Text message cannot be blank';
							return;
						}
						$scope.loading=true;
						$scope.successMsg = null;
						$scope.errorMessage = null;
						
						$scope.sendSMSWrapper = {
								guestId : $scope.selectedGuest.guestID,
								orgId : $scope.userDTO.organizationId,
								templateId : null,
								smsContent : $scope.smsContent,
								metrics :null,
								templateLevel:null
						}

						var postBody = $scope.sendSMSWrapper;
						console.log("sendSMS postbody-----"+JSON.stringify(postBody));
						var url = '/kyobee/web/rest/waitlistRestAction/sendSMS' ;
						KyobeeService.postDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.loading=false;
										$('#sendSMSPopup').simplePopup().hide();
										$(".simplePopupBackground").fadeOut("fast");
										$scope.loadWaitListPage(1);
										$scope.successMsg = "SMS Sent Successfully."
									} else if (data.status == "FAILURE") {
										$scope.loading=false;
										alert('Error while Sending SMS to Guest.');
									}
								}, function(error) {
									$scope.loading=false;
									alert('Error while Sending SMS to Guest.');
								});
						
					}
					
					$scope.cancelDelete = function(){
						$('#deletePopup').simplePopup().hide();
						$(".simplePopupBackground").fadeOut("fast");
						$scope.selectedGuest = null;
					}
					
					$scope.cancelSendSMS = function(){
						$('#sendSMSPopup').simplePopup().hide();
						$(".simplePopupBackground").fadeOut("fast");
						$scope.selectedGuest = null;
					}
					
					$scope.loadFactory = function(){
					
						$scope.loadOrtcFactory = loadOrtcFactory(IbtRealTimeSJType, function(factory, error) {
							$scope.client = factory.createClient();
							$scope.client.setClusterUrl($scope.connectionUrl);
							$scope.client.setConnectionMetadata('UserConnectionMetadata');
							$scope.client.onConnected = clientConnected;
							$scope.client.onSubscribed = clientSubscribed;
				            //client.onUnsubscribed = clientUnsubscribed;
							$scope.client.onReconnecting = clientReconnecting;
							$scope.client.onReconnected = clientReconnected;
							$scope.client.onDisconnected = clientDisconnected;
							$scope.client.onException = clientException;
							$scope.client.connect($scope.appKey, $scope.authToken);
				            function clientConnected(ortc) {
				                console.log('Connected to: ' + ortc.getUrl());
				                console.log('Subscribe to channel: ' + $scope.channel);
				                if ($scope.client.getIsConnected() == false) {
				                	$scope.client.connect($scope.appKey, $scope.authToken);
				                }
				                // Subscribe channel
				                ortc.subscribe($scope.channel, true, function onMessage(ortc, channel, message) {
				                	$scope.countMsgChannel++;
				                	$scope.countMsgChannel++;
				                    console.log('Received (' + $scope.countMsgChannel + '): ' + message + ' at channel: ' + channel);
				                    var m = jQuery.parseJSON(message);
				                    if (m.orgid == $scope.userDTO.organizationId) {
				                    	//$jquery("#totalParties").val(m.totalParties);
				                    	$scope.totalWaitTime = m.totalWaitTime;
				                    	//$jquery("#guestIdToBeNotifiedNext").val(m.guestIdToBeNotifiedNext);
				                    	//$jquery("#notifynusers").val(m.notifyUser);
				                    	if(m.OP != "NOTIFY_USER")
				                    	{ $scope.loadWaitListPage(1);}
				                    }
				                });
				            };
				            function clientSubscribed(ortc, channel) {
				                console.log('Subscribed to channel: ' + channel);
				            };
				            function clientUnsubscribed(ortc, channel) {
				                console.log('Unsubscribed from channel: ' + channel);
				            };
				            function clientReconnecting(ortc) {
				                console.log('Reconnecting to ' + $scope.connectionUrl);
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
						});
					}
					
					$scope.$watch('notifyFirst', function(newValue,oldValue) {
						if (newValue !== 'undefined' && newValue != null  && oldValue != newValue) {
							if(oldValue != null){
							 console.log("Watch New Value notifyFirst - " + newValue);
							 $scope.changeNotificationThreshold(newValue, oldValue);
							}
						} 
					});
					
					$scope.$watch('waitTime', function(newValue,oldValue) {
						if (newValue !== 'undefined' && newValue != null  && oldValue != newValue) {
							if(oldValue != null){
							 console.log("Watch New Value waitTime - " + newValue);
							 $scope.changePerPartyWaitTime(newValue, oldValue);
							}
						} 
					});
					
					$scope.changePerPartyWaitTime = function(newTime, oldTime){
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/changePerPartyWaitTime?orgid=' + $scope.userDTO.organizationId + '&numberofusers='+$scope.notifyFirst + '&perPartyWaitTime='+ newTime;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.totalWaitTime = data.serviceResult.ORG_TOTAL_WAIT_TIME;
									} else if (data.status == "FAILURE") {
										alert('Error while marking as seated.');
									}
								}, function(error) {
									alert('Error while marking as seated');
								});
						
					}
					
					$scope.changeNotificationThreshold = function(newTime, oldTime){
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/changeNotificationThreshold?orgid=' + $scope.userDTO.organizationId + '&numberofusers='+newTime + '&perPartyWaitTime='+ $scope.waitTime;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										/*alert("The top " + newTime + " parties will be notified in the event of any change in status.");*/
										$scope.totalWaitTime = data.serviceResult.ORG_TOTAL_WAIT_TIME;
									} else if (data.status == "FAILURE") {
										alert('Error while marking as seated.');
									}
								}, function(error) {
									alert('Error while marking as seated');
								});
						
					}
						
					
					
					$scope.loadInfo();
					$scope.homeCtrlLoaded.$promise.then(function(){
						$scope.loadWaitListPage(1);
					});
					
					$scope.$on('someEvent', function(e) {  
				        $scope.$parent.msg = $scope.loadWaitListPage(1);            
				    });
					/*$(document).ready(function(){
					    $("#slider-range").slider({
					    range: true,
					    min: 0,
					    max: 1440,
					    step: 15,
					    values: [600, 720],
					    slide: function (e, ui) {
					        var hours1 = Math.floor(ui.values[0] / 60);
					        var minutes1 = ui.values[0] - (hours1 * 60);

					        if (hours1.length == 1) hours1 = '0' + hours1;
					        if (minutes1.length == 1) minutes1 = '0' + minutes1;
					        if (minutes1 == 0) minutes1 = '00';
					        if (hours1 >= 12) {
					            if (hours1 == 12) {
					                hours1 = hours1;
					                minutes1 = minutes1 + " PM";
					            } else {
					                hours1 = hours1 - 12;
					                minutes1 = minutes1 + " PM";
					            }
					        } else {
					            hours1 = hours1;
					            minutes1 = minutes1 + " AM";
					        }
					        if (hours1 == 0) {
					            hours1 = 12;
					            minutes1 = minutes1;
					        }



					        $('.slider-time').html(hours1 + ':' + minutes1);
					        alert($scope.sliderStartTime);
					        $scope.sliderStartTime = hours1 + ':' + minutes1;
					        alert($scope.sliderStartTime);
					        
					        var hours2 = Math.floor(ui.values[1] / 60);
					        var minutes2 = ui.values[1] - (hours2 * 60);

					        if (hours2.length == 1) hours2 = '0' + hours2;
					        if (minutes2.length == 1) minutes2 = '0' + minutes2;
					        if (minutes2 == 0) minutes2 = '00';
					        if (hours2 >= 12) {
					            if (hours2 == 12) {
					                hours2 = hours2;
					                minutes2 = minutes2 + " PM";
					            } else if (hours2 == 24) {
					                hours2 = 11;
					                minutes2 = "59 PM";
					            } else {
					                hours2 = hours2 - 12;
					                minutes2 = minutes2 + " PM";
					            }
					        } else {
					            hours2 = hours2;
					            minutes2 = minutes2 + " AM";
					        }

					        $('.slider-time2').html(hours2 + ':' + minutes2);
					        
					    }
					});
					});*/
					
					$scope.setCountMessage = function(level){
						$scope.errorMessage=null;
						var text_max = 225;
						switch (level) {
						case 1:
							var text_length = $('#smsContent1').val().length;
							var text_remaining = text_max - text_length;
							$scope.countMessage1 = text_remaining + ' characters left';  
							if(text_length == 0){
								$scope.countMessage = 'Enter the text message';
							}
							break;
						case 2:
							var text_length = $('#smsContent2').val().length;
							var text_remaining = text_max - text_length;
							$scope.countMessage2 = text_remaining + ' characters left';  
							if(text_length == 0){
								$scope.countMessage = 'Enter the text message';
							}
							break;
						case 3:
							var text_length = $('#smsContent3').val().length;
							var text_remaining = text_max - text_length;
							$scope.countMessage3 = text_remaining + ' characters left';  
							if(text_length == 0){
								$scope.countMessage = 'Enter the text message';
							}
							break;
						default:
							break;
						}
						/*var text_length = $('#smsContent').val().length;
						var text_remaining = text_max - text_length;
						
						$scope.countMessage = text_remaining + ' characters left';
						  
						if(text_length == 0){
							$scope.countMessage = 'Enter the text message';
						}*/
					}
					
					 $scope.setPage=function(page) {
							if (page < 1 || page > $scope.pager.totalPages) {
								return;
							}

							// get pager object from service
							
							$scope.pager = 	KyobeeService.getPager($scope.totalGuestWaitList.length, page,$scope.pageSize);
							$scope.guestWaitList=$scope.totalGuestWaitList.slice($scope.pager.startIndex, $scope.pager.endIndex + 1);
							
							
						}
				} ]);

KyobeeControllers.filter('hourMinFilter', function () {
    return function (value) {
    	return value+':00 Hrs';
    	/*if(value.length >= 9){
    		return value + ':00 Hrs';
    	}
    	else{
    		return value+':00 Hrs';
    	}*/
    };
});