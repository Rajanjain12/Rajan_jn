KyobeeUnSecuredController.controller('guestDetailCtrl',
		[
				'$scope',
				'$location',
				'$timeout',
				'$interval',
				'$routeParams',
				'KyobeeUnsecuredService',
				function($scope, $location, $timeout, $interval, $routeParams, KyobeeUnsecuredService) {
					
					$scope.guest = null;
					$scope.tid = null;
					$scope.seatPrefs = null;
					$scope.totalWaitTime = null;
					$scope.guestRankMin = null;
					$scope.guestAheadCount = null;
					$scope.orgWaitTime = null;
					
					$scope.appKey = null;
					$scope.privateKey = null;
					$scope.channel = null;
					$scope.authToken = 'Unno1adyiSooIAkAEt';
					$scope.connectionUrl = 'https://ortc-developers.realtime.co/server/2.1';
					$scope.client = null;
					$scope.authenticationRequired = false;
					$scope.isCluster = true;
					$scope.countMsgChannel = 0;
					
					$scope.selectedSeatPref = [];
					
					$scope.loadGuestPage = function(){
						
						var postBody = {};
						var url = "/kyobee/web/rest/waitlistRestAction/guestuuid?uuid="+$scope.tid;
						 
						KyobeeUnsecuredService.getDataService(url , postBody)
							.query(postBody, function(data) {
								console.log(data);
								if (data.status == "SUCCESS") {
									$scope.guest = data.serviceResult;
									$scope.selectedSeatPref = [];
									if($scope.guest.seatingPreference != null && $scope.guest.seatingPreference != 'undefined'){
										var seatingPrefs = $scope.guest.seatingPreference.split(',');
										if(seatingPrefs != null && seatingPrefs != 'undefined'){
											for(var i=0; i<seatingPrefs.length; i++){
												$scope.selectedSeatPref.push(seatingPrefs[i]);
											}
										}
										console.log($scope.selectedSeatPref);
									}
									$scope.loadSeatingPref($scope.guest.organizationID);
									$scope.loadUserMetricks($scope.guest.organizationID, $scope.guest.guestID);
								} else if (data.status == "FAILURE") {
									console.log(data.serviceResult);
								}
							}, function(error) {
								console.log(error);
							});
						
					}
					
					$scope.loadSeatingPref = function(orgId) {
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/orgseatpref?orgid=' + orgId;
						KyobeeUnsecuredService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.seatPrefs = data.serviceResult;
									} else if (data.status == "FAILURE") {
										alert('Error while fetching user details. Please login again or contact support');
										$scope.logout();
									}
								}, function(error) {
									alert('Error while fetching user details. Please login again or contact support');
								});
					};
					
					$scope.updateGuest = function(){
						
						$scope.errorMsg = null;
						
						/*if(!invalid){
							return;
						}*/
						
						if($scope.guest.prefType == null || $scope.guest.prefType == 'undefined'){
							$scope.errorMsg = "Please select sms or email";
							return;
						}
						
						if(($scope.guest.prefType == 'sms' || $scope.guest.prefType == 'SMS') && ($scope.guest.sms == null || $scope.guest.sms == 'undefined')){
							$scope.errorMsg = "Please enter the contact no.";
							return;
						}
						
						if(($scope.guest.prefType == 'email' || $scope.guest.prefType == 'EMAIL') && ($scope.guest.email == null || $scope.guest.email == 'undefined')){
							$scope.errorMsg = "Please enter the email";
							return;
						}
						
						var selectedGuestPref = [];
						
						for(i=0;i<$scope.seatPrefs.length;i++){
							for(j=0; j<$scope.selectedSeatPref.length;j++){
								if($scope.seatPrefs[i].prefValueId == $scope.selectedSeatPref[j]){
									selectedGuestPref.push($scope.seatPrefs[i]);
									break;
								}
							}
							
						}
						
						var postBody = {
								'name' : 	$scope.guest.name,
								'guestID' : $scope.guest.guestID,
								'organizationID' : $scope.guest.OrganizationID,
								'noOfPeople' : $scope.guest.noOfPeople,
								'prefType' : $scope.guest.prefType,
								'sms' : $scope.guest.sms,
								'email' : $scope.guest.email,
								'optin' : $scope.guest.optin,
								'status': 'CHECKIN',
								'guestPreferences' : selectedGuestPref
						}
						
						var url = '/kyobee/web/rest/waitlistRestAction/updateGuestInfo';
						KyobeeUnsecuredService.postService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										if ($scope.client.getIsConnected() == false) {
											$scope.client.connect($scope.appKey, $scope.authToken);
					                     }
					   	    		    var message = JSON.stringify({"OP":"UpdageGuestInfo","guestObj":data.serviceResult.updguest.guestID,"updguest":data.serviceResult.guest,"FROM":"USER","ppwt":$scope.orgWaitTime ,"orgid":$scope.guest.organizationID});
					                    $scope.client.send($scope.channel, message);
							            console.log('Sending from updateguest: ' + message + ' to channel: ' + $scope.channel);
										alert('Guest Inforation updated successfully');
									} else if (data.status == "FAILURE") {
										alert('Error while updating guest');
									}
								}, function(error) {
									alert('Error while updating guest');
								});
					}
					
					$scope.deleteGuest = function(){
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/deleteGuest?orgId=' + $scope.guest.organizationID + '&guestId='+$scope.guest.guestID;
						KyobeeUnsecuredService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										if ($scope.client.getIsConnected() == false) {
											$scope.client.connect($scope.appKey, $scope.authToken);
					                     }
					   	    		    var message = JSON.stringify( {"OP":"DEL","guestObj":$scope.guest.guestID,"FROM":"USER","ppwt":$scope.orgWaitTime,"orgid":$scope.guest.organizationID});
					   	    		    //$scope.client.send($scope.channel, message);
							            //console.log('Sending from deleteguest: ' + message + ' to channel: ' + $scope.channel);
						   	    	 	//$('#deletemsg').addClass('is-visible');
						   	    	 
										$('#deletePopup').simplePopup().hide();
										$(".simplePopupBackground").fadeOut("fast");
										$scope.guest = null;
										//$scope.loadWaitListPage(1);
									} else if (data.status == "FAILURE") {
										alert('Error while deleting guest.');
									}
								}, function(error) {
									alert('Error while deleting guest.');
								});
						
					}
					
					$scope.showDeletePopup = function(){
						$('#deletePopup').simplePopup();
					}
					
					$scope.cancelDelete = function(){
						$('#deletePopup').simplePopup().hide();
						$(".simplePopupBackground").fadeOut("fast");
					}
					
					$scope.loadUserMetricks = function(orgId, guestId) {
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/usermetricks?guest='+guestId+'&orgid='+orgId;
						KyobeeUnsecuredService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.totalWaitTime = data.serviceResult.TOTAL_WAIT_TIME;
										$scope.guestRankMin = data.serviceResult.GUEST_RANK_MIN;
										$scope.guestAheadCount = data.serviceResult.GUEST_AHEAD_COUNT;
										$scope.orgWaitTime = data.serviceResult.ORG_WAIT_TIME;
									} else if (data.status == "FAILURE") {
										alert('Error while fetching user details. Please login again or contact support');
										$scope.logout();
									}
								}, function(error) {
									alert('Error while fetching user details. Please login again or contact support');
								});
					};
					
					$scope.init = function(){
						
						if ($routeParams.tid != null && $routeParams.tid != 'undefined'){
							$scope.tid = $routeParams.tid;
							$scope.loadGuestPage();
						}
						
						$scope.loadInfo();
						
					}
					
					$scope.loadInfo = function() {
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/pusgerinformation';
						KyobeeUnsecuredService.getDataService(url, '').query(postBody,
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
								// Subscribe channel
								ortc.subscribe($scope.channel, true, function onMessage(ortc, channel, message) {
								    $scope.countMsgChannel++;
								    console.log('Received (' + $scope.countMsgChannel + '): ' + message+ ' at channel: ' + channel);
									var m = jQuery.parseJSON(message);
									
									if(m.orgid == $scope.guest.organizationID){ 
										
										if(m.OP=='DEL'){
										    	var nbp = $scope.guestAheadCount;
										    	$scope.guestAheadCount = $scope.guestAheadCount - 1;
												var ppwt = $scope.orgWaitTime;
												$scope.totalWaitTime = ppwt*(nbp-1) + parseInt(ppwt);
												$scope.$apply();
										    }
									    if(m.OP=='PPT_CHG'){
									    	var nbp = $scope.guestAheadCount;
											var ppwt = parseInt(m.ppwt);
											$scope.totalWaitTime = (ppwt*nbp)+parseInt(ppwt);
											$scope.$apply();
										    }
									    if(m.OP=='RESET'){ 
									    	location.reload();
										    }
									    if(m.OP=='MINRANK'){ 
									    	 $("#guestRankMin").html(m.guminrank);
											 //Start for Prefix
									    	 if($.query.get("orgid") == 15){
									 			 var grmin = parseInt($("#guestRankMin").html());
									  			  	$("#guestRankMin").html("A"+grmin);
										      	}
										     //End for prefix
										    }
									    if(m.OP=='UPD'){
								            console.log("update received from Guest")
								        }
	
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
					
					
					$scope.init();
					
					

				} ]);
