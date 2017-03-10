
KyobeeControllers.controller('partyWaitingCtrl',
		[
				'$scope',
				'$rootScope',
				'$location',
				'$timeout',
				'$interval',
				'KyobeeService',
				function($scope,$rootScope, $location, $timeout, $interval, KyobeeService) {

					$rootScope.hideHeader=true;//To hide show header in index.html
					console.log("Parties Watiing..");
					
					$scope.OrgGuestCount=0;
					$scope.guestRankMin=0;
					$scope.OrgTotalWaitTime=null;
					$scope.hour=0;
					$scope.min=0;
					$scope.phnRegex="^([0-9]{3}|[0-9]{3})[0-9]{3}[0-9]{4}$";
					$scope.footerMsg="Copyright KYOBEE. All Rights Reserved";
					$scope.loading=false;
					
					$scope.guestDTO = {
							name: null,
							organizationID : $scope.userDTO.organizationId,
							noOfPeople : null,
							prefType : null,
							email : null,
							sms : null,
							optin : false,
							note : null
					};
					$scope.guestRank=0;
					$scope.guestPref = null;
					$scope.errorMsg = null;
					
					$scope.getData = function () {
						
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/totalwaittimemetricks?orgid=' + $scope.userDTO.organizationId;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										//$scope.metricsDTO = data.serviceResult;
										$scope.OrgGuestCount=data.serviceResult.ORG_GUEST_COUNT;
										$scope.guestRankMin=data.serviceResult.GUEST_RANK_MIN;
										$scope.OrgTotalWaitTime=data.serviceResult.ORG_TOTAL_WAIT_TIME;
										var min=$scope.OrgTotalWaitTime;
										var h = Math.floor(min/60);
										h = h.toString().length == 1 ? (0+h.toString()) : h ;
										$scope.hour=h;
										var m = min%60;
										m = m.toString().length == 1 ? (0+m.toString()) : m ;
										$scope.min=m;
										//return h + ":" + m;
									} else if (data.status == "FAILURE") {
										alert('Error while fetching details.');
										$scope.logout();
									}
								}, function(error) {
									alert('Error while fetching details.');
								});
						
					};
				
					$scope.getHours = function(min) {
						var h = Math.floor(min/60);
						h = h.toString().length == 1 ? (0+h.toString()) : h ;
						return h;
					}
					
					$scope.getMinutes = function(min) {
						var m = min%60;
						m = m.toString().length == 1 ? (0+m.toString()) : m ;
						return m;
					}
					
					
					$scope.getData();
					
					$scope.showPopup =function() {
						$('#pop1').simplePopup();
						$scope.initAddGuest();
						$scope.guestPref = angular.copy($scope.seatPrefs);
						console.log("GuestPRef popup : "+ JSON.stringify($scope.guestPref));
					}
					
					$scope.hidePopup = function() {		
						//document.getElementById("addGuestForm").reset();
						debugger;
						$scope.guestDTO = {
								name: null,
								organizationID : $scope.userDTO.organizationId,
								noOfPeople : null,
								prefType : null,
								email : null,
								sms : null,
								optin : false,
								note : null
						};
						 $('#emailhide').hide();
						 $('#smshide').hide();
						 $scope.loadSeatingPref();
						$scope.addGuestForm.$setPristine();
						$scope.addGuestForm.$setUntouched();
						$('#pop2').simplePopup().hide();
						$(".simplePopupBackground").fadeOut("fast");
						//$scope.changeView('home');
					}
					
					
					//$scope.editMode = false;					
					
					$scope.initAddGuest = function(){
						
						$scope.errorMsg = null;
						
						// we have loaded the seat prefs in main controller to avoid loading again & again on child controller. 
						//But if user refreshes page or directly goes to add page via url, 
						//then parent controller seat prefs are not available. Thus fetching them explicitly on those scenarios.
						if($scope.seatPrefs == null || $scope.seatPrefs == undefined){
							$scope.loadSeatingPref();
						} else {
							$scope.guestPref = angular.copy($scope.seatPrefs);
							console.log("GuestPRef : "+ JSON.stringify($scope.guestPref));
						}
						
					}
					
					$scope.loadSeatingPref = function() {
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/orgseatpref?orgid=' + $scope.userDTO.organizationId;;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log("orgseatpref"+data);
									if (data.status == "SUCCESS") {
										$scope.guestPref = data.serviceResult;
										$scope.seatPrefs = data.serviceResult;
										console.log("GuestPRef : "+ $scope.guestPref);
									} else if (data.status == "FAILURE") {
										alert('Error while fetching user details. Please login again or contact support');
										$scope.logout();
									}
								}, function(error) {
									alert('Error while fetching user details. Please login again or contact support');
								});
						
						console.log("GuestPRef : "+ JSON.stringify($scope.guestPref));
					};
					
					$scope.hideErrorMsg = function(){
						$scope.errorMsg = null;
					}
					
					$scope.addGuest = function(invalid){
						
						$scope.errorMsg = null;
						$scope.loading=true;
						
						if(invalid){
							$scope.loading=false;
							return;
						}
						
						if($scope.guestDTO.prefType == null || $scope.guestDTO.prefType == 'undefined'){
							$scope.errorMsg = "Please select sms or email";
							$scope.loading=false;
							return;
						}
						
						if(($scope.guestDTO.prefType == 'sms' || $scope.guestDTO.prefType == 'SMS') && ($scope.guestDTO.sms == null || $scope.guestDTO.sms == 'undefined')){
							$scope.errorMsg = "Please enter the contact no.";
							$scope.loading=false;
							return;
						}
						
						if(($scope.guestDTO.prefType == 'email' || $scope.guestDTO.prefType == 'EMAIL') && ($scope.guestDTO.email == null || $scope.guestDTO.email == 'undefined')){
							$scope.errorMsg = "Please enter the email";
							$scope.loading=false;
							return;
						}
						
						var selectedGuestPref = [];
						
						for(i=0;i<$scope.seatPrefs.length;i++){
							if($scope.seatPrefs[i].selected){
								selectedGuestPref.push($scope.seatPrefs[i]);
							}
						}
						$scope.guestDTO.guestPreferences = selectedGuestPref;
						console.log($scope.guestDTO);
						
						var postBody = $scope.guestDTO;
						
						var url = '/kyobee/web/rest/waitlistRestAction/addGuest';
						KyobeeService.postDataService(url, '').query(postBody,
								function(data) {
									console.log("Submitted data for add guest : "+JSON.stringify(data));
									if (data.status == "SUCCESS") {
										//$scope.changeView('home');
										$('#pop1').simplePopup().hide();
										$scope.guestRank=data.serviceResult.guestRank;
										$scope.loading=false;
										$('#pop2').simplePopup();
									} else if (data.status == "FAILURE") {
										alert('Error while adding guest to waitlist');
									}
								}, function(error) {
									alert('Error while adding guest to waitlist');
								});
						
						$('#pop1').simplePopup().hide();					
						
					}
					
					$scope.convertMinstoMMHH = function(min){
						var h = Math.floor(min/60);
						h = h.toString().length == 1 ? (0+h.toString()) : h ;
						var m = min%60;
						m = m.toString().length == 1 ? (0+m.toString()) : m ;
						return h + ":" + m;
					}
					
					
					// For realtime 
					
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
										$scope.footerMsg=data.serviceResult.footerMsg;
										$scope.loadFactory();
									} else if (data.status == "FAILURE") {
										alert('Error while fetching user details. Please login again or contact support');
										$scope.logout();
									}
								}, function(error) {
									alert('Error while fetching user details. Please login again or contact support');
								});
					};
					
					$scope.loadFactory = function(){
						
						$scope.loadOrtcFactory = loadOrtcFactory(IbtRealTimeSJType, function (factory, error){
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
							if($scope.authenticationRequired){
							// Enable presence data for MyChannel
								$scope.client.enablePresence({
									applicationKey : $scope.appKey,
									channel : $scope.channel, 
									privateKey : $scope.privateKey, 
									url : $scope.connectionUrl,
									isCluster : $scope.isCluster,
									metadata : 1
								},
								function(error,result){
									if(error){
										console.log('Enable presence error: ' + error);  
									}else{
										console.log('Presence enable: ' + result);              
									}
								});
							}

							/* 
							* Function connected to realtime.co framework and it is the main function for developers  
							* 
							*/

							function clientConnected(ortc) {
												console.log('Connected to: ' + ortc.getUrl());
								console.log('Subscribe to channel: ' + $scope.channel);
	
	
								if ($scope.client.getIsConnected() == false) {
								    $scope.client.connect($scope.appKey, $scope.authToken);
								 }
								console.log("Channel name : "+$scope.channel);
								// Subscribe channel
								ortc.subscribe($scope.channel, true, function onMessage(ortc, channel, message) {
							    $scope.countMsgChannel++;
							    console.log('Received (' + $scope.countMsgChannel + '): ' + message+ ' at channel: ' + channel);
								var m = jQuery.parseJSON(message);
						
								/*if(m.orgid == $scope.guest.organizationID){ */
									
									
								switch(m.OP) {
							        case "DEL":							        	
							        	$scope.OrgGuestCount=m.numberofparties;
										$scope.guestRankMin=m.nowServingParty;
										$scope.OrgTotalWaitTime=m.ORG_TOTAL_WAIT_TIME;
										$scope.hour=$scope.getHours($scope.OrgTotalWaitTime);
										$scope.min=$scope.getMinutes($scope.OrgTotalWaitTime);
										$scope.$apply();
										break;
							        case "ADD":
							        	$scope.OrgGuestCount=m.totalPartiesWaiting;
										$scope.guestRankMin=m.nowServingParty;
										$scope.OrgTotalWaitTime=m.ORG_TOTAL_WAIT_TIME;
										$scope.hour=$scope.getHours($scope.OrgTotalWaitTime);
										$scope.min=$scope.getMinutes($scope.OrgTotalWaitTime);
										$scope.$apply();
								        break;
							        case "UPD":
							        	$scope.OrgGuestCount=m.ORG_GUEST_COUNT;
										$scope.guestRankMin=m.NOW_SERVING_GUEST_ID;
										$scope.OrgTotalWaitTime=m.ORG_TOTAL_WAIT_TIME;
										$scope.hour=$scope.getHours($scope.OrgTotalWaitTime);
										$scope.min=$scope.getMinutes($scope.OrgTotalWaitTime);
										$scope.$apply();
							        	break;
							        case "MARK_AS_SEATED":
							        	$scope.OrgGuestCount=m.ORG_GUEST_COUNT;
										$scope.guestRankMin=m.nowServingParty;
										$scope.OrgTotalWaitTime=m.ORG_TOTAL_WAIT_TIME;
										$scope.hour=$scope.getHours($scope.OrgTotalWaitTime);
										$scope.min=$scope.getMinutes($scope.OrgTotalWaitTime);
										$scope.$apply();
							        	break;
							        default:
							        	console.log("Wrong op!");
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
							console.log('Disconnected');
							};

							function clientException(ortc, error) {
							console.log('Error: ' + error);
							};
							console.log('Connecting to ' + $scope.connectionUrl);
							// Connect to the Realtime Framework cluster
							//$scope.client.connect($scope.appKey, $scope.authToken);


							});
					}
					
					
					$scope.loadInfo();
					//To hide popup on esc key
					$scope.hideOnKeyPress = function(keyEvent) {
						if (keyEvent.which === 27) {											
							$('#pop1').simplePopup().hide();
							$scope.hidePopup();
							$(".simplePopupBackground").fadeOut("fast");
							
						}
					}
					/*$scope.init = function(){
						$scope.getData();
					};
					
					$scope.init();*/
					
				} ]);
