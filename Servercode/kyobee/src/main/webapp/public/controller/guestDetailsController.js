KyobeeUnSecuredController.controller('guestDetailCtrl',
		[
				'$scope',
				'$q',
				'$location',
				'$timeout',
				'$interval',
				'$routeParams',
				'KyobeeUnsecuredService',
				function($scope, $q, $location, $timeout, $interval, $routeParams, KyobeeUnsecuredService) {
					
					$scope.guest = null;
					$scope.tid = null;
					$scope.seatPrefs = null;
					$scope.totalWaitTime = null;
					$scope.guestRankMin = null;
					$scope.guestAheadCount = null;
					$scope.orgWaitTime = null;
					$scope.errorMsg = null;
					
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
					$scope.loading = false;
					
					$scope.currentPageLanguage = null;
					
					//creating new key-value jason for multilingual updateguest page
					$scope.pageLanguage = {
						  "en" : {
						    "nowServing" : "Now Serving",
						    "partiesWaiting" : "Parties Waiting",
						    "estWaitTime" : "Est. Wait Time",
						    "seatingPref" : "Seating Preference",
						    "optInReceive" : "(Opt-in to receive promotions/special)",
						    "rmvWaitlist" : "REMOVE FROM WAITLIST",
						    "saveChanges" : "SAVE CHANGES",
						    "enterNameError" : "Error! Please enter a valid name",
						    "enterNumError" : "Error ! Please enter the contact no.",
						    "adultSizeError" : "Error ! Adults must be greater than 0",
						    "selectSmsError" : "Error ! Please select sms or Email",
						    "enterEmailError" : "Error ! Please enter the Email",
						    "validNumError" : "please enter the valid number",
						    "fetchError" : "Error while fetching user details. Please login again or contact support",
						    "updSuccess" : "Guest Information updated successfully",
						    "updError" : "Error while updating Guest",
						    "dltError" : "Error while deleting Guest"
						  },
						  "chi" : {
						    "nowServing" : "正在服務",
						    "partiesWaiting" : "等待人數",
						    "estWaitTime" : "預計等候時間",
							"seatingPref" : "桌位偏好",
							"optInReceive" : "(同意接受優惠信息)",
							"rmvWaitlist" : "從排隊名單中去除",
							"saveChanges" : "保存修改",
							"enterNameError" : "错误！请输入有效的名字",
							"enterNumError" : "错误！请输入联络号码",
							"adultSizeError" : "错误！成人必须大于0",
							"selectSmsError" : "错误！请选择短信或电子邮件",
						    "enterEmailError" : "错误！请输入电子邮件",
							"validNumError" : "请输入有效的号码",
							"fetchError" : "获取用户详细信息时出错。请再次登录或联系支持",
							"updSuccess" : "客人信息已成功更新",
							"updError" : "更新来宾时出错",
							"dltError" : "删除访客时出错"
						  }
						}
					
					$scope.loadGuestPage = function(){
						var defered=$q.defer();
						var postBody = {};
						var url = "/kyobee/web/rest/waitlistRestAction/guestuuid?uuid="+$scope.tid;
						 
						KyobeeUnsecuredService.getDataService(url , postBody)
							.query(postBody, function(data) {
								console.log(data);
								if (data.status == "SUCCESS") {
									$scope.guest = data.serviceResult;
									console.log($scope.guest);
									if($scope.guest == null || $scope.guest == 'undefined'){
										return;
									}
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

									if($scope.guest.prefType == 'sms' || $scope.guest.prefType == 'SMS'){
										$scope.guest.sms = Number.parseInt($scope.guest.sms);
										$scope.guest.prefType = 'sms';
										showsms1();
									} else if($scope.guest.prefType == 'email' || $scope.guest.prefType == 'EMAIL') {
										showemail1();
										$scope.guest.prefType = 'email';
									}
									
									$scope.loadSeatingPref($scope.guest.organizationID);
									$scope.loadUserMetricks($scope.guest.organizationID, $scope.guest.guestID);
									defered.resolve();
								} else if (data.status == "FAILURE") {
									console.log(data.serviceResult);
								}
							}, function(error) {
								console.log(error);
								defered.reject();
							});
						return defered.promise;
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
										alert($scope.currentPageLanguage.fetchError);
										$scope.logout();
									}
								}, function(error) {
									alert($scope.currentPageLanguage.fetchError);
								});
					};
					
					$scope.updateGuest = function(){
						
						$scope.loading = true;
						$scope.errorMsg = null;
						
						/*if(!invalid){
							return;
						}*/
						
						if($scope.guest.name == null || $scope.guest.name == 'undefined' || $scope.guest.name == ''){
							$scope.errorMsg = $scope.currentPageLanguage.enterNameError;
							$scope.loading = false;
							return;
						}
						
						if($scope.guest.prefType == null || $scope.guest.prefType == 'undefined'){
							$scope.errorMsg = $scope.currentPageLanguage.selectSmsError;
							$scope.loading = false;
							return;
						}
						
						if(($scope.guest.prefType == 'sms' || $scope.guest.prefType == 'SMS') && ($scope.guest.sms == null || $scope.guest.sms == 'undefined' || $scope.guest.sms == "")){
							$scope.errorMsg = $scope.currentPageLanguage.enterNumError;
							$scope.loading = false;
							return;
						}
						
						if(($scope.guest.prefType == 'email' || $scope.guest.prefType == 'EMAIL') && ($scope.guest.email == null || $scope.guest.email == 'undefined')){
							$scope.errorMsg = $scope.currentPageLanguage.enterEmailError;
							$scope.loading = false;
							return;
						}
						
						if(($scope.guest.noOfAdults != null && $scope.guest.noOfAdults == 0 || $scope.guest.noOfAdults == undefined)){
							$scope.errorMsg = $scope.currentPageLanguage.adultSizeError;
							$scope.loading=false;
							return;
						}
						
						if ($("#children").val() == "") {
						     $("#children").val("0");
						}
					
						if ($("#infants").val() == ""){
							$("#infants").val("0");
						}
						
						var selectedGuestPref = [];
						if($scope.seatPrefs!=null) {
							for(i=0;i<$scope.seatPrefs.length;i++){
								for(j=0; j<$scope.selectedSeatPref.length;j++){
									if($scope.seatPrefs[i].prefValueId == $scope.selectedSeatPref[j]){
										selectedGuestPref.push($scope.seatPrefs[i]);
										break;
									}
								}								
							}
						}
						
						var postBody = {
								'name' : 	$scope.guest.name,
								'guestID' : $scope.guest.guestID,
								'organizationID' : $scope.guest.OrganizationID,
								'noOfChildren' : $scope.guest.noOfChildren,
								'noOfAdults' : $scope.guest.noOfAdults,
								'noOfInfants' : $scope.guest.noOfInfants,
								'noOfPeople' : $scope.guest.noOfAdults+$scope.guest.noOfChildren+$scope.guest.noOfInfants,
								'prefType' : $scope.guest.prefType,
								'sms' : $scope.guest.sms,
								'email' : $scope.guest.email,
								'optin' : $scope.guest.optin,
								'status': 'CHECKIN',
								'guestPreferences' : selectedGuestPref,
								'languagePref' : {'langId':$scope.guest.languagePrefID}
						}
						console.log(JSON.stringify(postBody));
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
							            $scope.loading = false;
										alert($scope.currentPageLanguage.updSuccess);
									} else if (data.status == "FAILURE") {
										alert($scope.currentPageLanguage.updError);
									}
								}, function(error) {
									alert($scope.currentPageLanguage.updError);
								});
					}
					
					$scope.deleteGuest = function(){
						$scope.loading = true;
						var postBody = $scope.guest;
						var url = '/kyobee/web/rest/waitlistRestAction/deleteGuest';
						KyobeeUnsecuredService.postDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.loading= false;
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
										alert($scope.currentPageLanguage.dltError);
									}
								}, function(error) {
									alert($scope.currentPageLanguage.dltError);
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
										alert($scope.currentPageLanguage.fetchError);
										$scope.logout();
									}
								}, function(error) {
									alert($scope.currentPageLanguage.fetchError);
								});
					};
					
					$scope.init = function(){
						
						if ($routeParams.tid != null && $routeParams.tid != 'undefined'){
							$scope.tid = $routeParams.tid;
							var promise = $scope.loadGuestPage();
							promise.then(function(){
								debugger;
								if($scope.guest.languagePrefID == 1){
									$scope.currentPageLanguage = $scope.pageLanguage.en;
								}
								else if($scope.guest.languagePrefID == 134){
									$scope.currentPageLanguage = $scope.pageLanguage.chi;
								}
							},function(error){
								
							});
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
										alert($scope.currentPageLanguage.fetchError);
										$scope.logout();
									}
								}, function(error) {
									alert($scope.currentPageLanguage.fetchError);
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
					
					
					$scope.hideErrorMsg = function(){
					       $scope.errorMsg = null;
					      };
					
					$scope.init();
					
					

				} ]);
