
KyobeeControllers.controller('waitListCtrl',
		[
				'$scope',
				'$rootScope',
				'$location',
				'$timeout',
				'$interval',
				'KyobeeService',
				function($scope, $rootScope,$location, $timeout, $interval, KyobeeService) {
						
					/*$scope.sliderStartTime = 'hello';*/
					$rootScope.hideHeader=false;//To hide show header in index.html
					$scope.waitTime = null;
					$scope.notifyFirst = null;
					$scope.totalWaitTime = null;
					$scope.guestWaitList = null;
					$scope.selectedGuest = null;
					$scope.client = null;
					$scope.countMsgChannel = 0;
					
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
					
					$scope.statusOptions=["All","Not Present","Incomplete"];
					$scope.selectedStatus=$scope.statusOptions[0];
					
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
							/*optIn : true,*/
							note : true,
							del : true
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
									console.log(data);
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
						/*case 'optIn':
							if($scope.toggleColumn.optIn == true){
								$scope.toggleColumn.optIn = false; 
							}
							else{
								$scope.toggleColumn.optIn = true;
							}
							break;*/
							
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
							
						default:
							break;
						}
					}
					
					$scope.loadWaitListPage = function(pageNo){						
						console.log(pageNo);
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
						$scope.loadOrgMetricks();
						var url = '/kyobee/web/rest/waitlistRestAction/checkinusers';
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log("Postbody "+JSON.stringify(postBody));
									console.log("Waitlist Grid data : "+ JSON.stringify(data));
									if (data.status == "SUCCESS") {
										var paginatedResponse = data.serviceResult;
										$scope.guestWaitList = paginatedResponse.records;
										$scope.pager = 	KyobeeService.getPager(paginatedResponse.totalRecords, pageNo, $scope.pageSize);
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
									console.log("Postbody "+JSON.stringify(postBody));
									console.log("Waitlist Grid data : "+ JSON.stringify(data));
									if (data.status == "SUCCESS") {
										var paginatedResponse = data.serviceResult;
										$scope.guestWaitList = paginatedResponse.records;
										$scope.pager = 	KyobeeService.getPager(paginatedResponse.totalRecords, paginatedResponse.pageNo, $scope.pageSize);
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
						
						console.log($scope.selectedStatus);
						
						var d = new Date(); // or whatever date you have
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
						/*tzName = d.toLocaleString('en', {timeZoneName:'short'}).split(' ').pop();*/
						tzName = createOffset(d);
						/*alert("krupali"+tzName);*/
						
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
										$scope.guestWaitList = paginatedResponse.records;
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
						/*debugger;*/
						
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
								sliderMaxTime : $scope.slider.maxTime
						};
						
						//$scope.loadOrgMetricks();
						var url = '/kyobee/web/rest/waitlistRestAction/searchhistoryuser';
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										var paginatedResponse = data.serviceResult;
										$scope.guestWaitList = paginatedResponse.records;
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
						$scope.selectedGuest = guestObj;
					}
					
					$scope.showDeletePopup = function(guestObj){
						$('#deletePopup').simplePopup();
						$scope.selectedGuest = guestObj;
					}
					
					$scope.incrementCalloutCount = function(){
						$scope.successMsg = null;
						var postBody = $scope.selectedGuest;
						
						var url = '/kyobee/web/rest/waitlistRestAction/incrementCalloutCount';
						KyobeeService.postDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
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
						$scope.successMsg = null;
						var postBody = $scope.selectedGuest;
						
						var url = '/kyobee/web/rest/waitlistRestAction/markAsIncomplete';
						KyobeeService.postDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
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
						$scope.successMsg = null;
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/markAsSeated?orgId=' + $scope.userDTO.organizationId + '&guestId='+$scope.selectedGuest.guestID;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
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
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/deleteGuest?orgId=' + $scope.userDTO.organizationId + '&guestId='+$scope.selectedGuest.guestID;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$('#deletePopup').simplePopup().hide();
										$(".simplePopupBackground").fadeOut("fast");
										$scope.loadWaitListPage(1);
										$scope.successMsg = "Guest Deleted Successfully."
									} else if (data.status == "FAILURE") {
										alert('Error while deleting guest.');
									}
								}, function(error) {
									alert('Error while deleting guest.');
								});
						
					}
					
					$scope.cancelDelete = function(){
						$('#deletePopup').simplePopup().hide();
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
				                console.log('Reconnecting to ' + connectionUrl);
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
										alert("The top " + newTime + " parties will be notified in the event of any change in status.");
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