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
					$scope.marketingPref = null;
					$scope.totalWaitTime = null;
					$scope.guestRankMin = null;
					$scope.guestAheadCount = null;
					$scope.orgWaitTime = null;
					$scope.orgMaxParty = null;
					$scope.errorMsg = null;
					$scope.successMsg = null;
					
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
					$scope.selectedGuestMarketingPref = [];
					$scope.loading = false;
					
					$scope.currentPageLanguage = null;
					
					//creating new key-value json for multilingual updateguest page
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
						    "dltError" : "Error while deleting Guest",
						    "confirmDel" : "Confirm Delete Guest",
						    "dltButton" : "DELETE",
						    "cnclButton" : "CANCEL",
						    "notExist" : "The requested guest doesn't exist anymore.",
						    "orgMaxParty1" : "Please check in with host for parties larger than ",
						    "orgMaxParty2" : " people."
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
							"dltError" : "删除访客时出错",
							"confirmDel" : "确认删除访客",
						    "dltButton" : "删除",
						    "cnclButton" : "取消",
						    "notExist" : "被请求的客人不存在了。",
						    "orgMaxParty1" : "请与主办方检查大于 ",
						    "orgMaxParty2" : " 人。"
						  },
						  "zhHant" : {
					           "nowServing" : "現在服務",
					           "partiesWaiting" : "各方等待",
					           "estWaitTime" : "東。等待時間",
					           "seatingPref" : "座位偏好",
					           "optInReceive" : "（選擇接收促銷/特價）",
					           "rmvWaitlist" : "從WAITLIST中刪除",
					           "saveChanges" : "保存更改",
					           "enterNameError" : "錯誤！請輸入一個有效的名字",
					           "enterNumError" : "錯誤！請輸入聯繫號碼。",
					           "adultSizeError" : "錯誤！成人必須大於0",
					           "selectSmsError" : "錯誤！請選擇短信或電子郵件",
					           "enterEmailError" : "錯誤！請輸入電子郵件",
					           "validNumError" : "請輸入有效號碼",
					           "fetchError" : "獲取用戶詳細信息時出錯。請重新登錄或聯繫支持",
					           "updSuccess" : "訪客信息已成功更新",
					           "updError" : "更新Guest時出錯",
					           "dltError" : "刪除Guest時出錯",
					           "confirmDel" : "確認刪除訪客",
					           "dltButton" : "刪除",
					           "cnclButton" : "取消",
					           "notExist" : "請求的客人不再存在。",
					           "orgMaxParty1" : "請與主辦方聯繫，以便舉辦大於",
					           "orgMaxParty2" : " 人。"
					         },
						  "ko" : {
							    "nowServing" : "이번 손님",
							    "partiesWaiting" : "파티 대기",
							    "estWaitTime" : "대기시간",
							    "seatingPref" : "좌석 선호도",
							    "optInReceive" : "(프로모션 / 특별 혜택 받기)",
							    "rmvWaitlist" : "대기 목록에서 제거",
							    "saveChanges" : "변경 사항을 저장하다",
							    "enterNameError" : "오류! 올바른 이름을 입력하십시오.",
							    "enterNumError" : "오류! 연락처 번호를 입력하십시오.",
							    "adultSizeError" : "오류! 성인은 0보다 커야합니다.",
							    "selectSmsError" : "오류! SMS 또는 전자 메일을 선택하십시오.",
							    "enterEmailError" : "오류! 이메일을 입력하십시오.",
							    "validNumError" : "유효한 번호를 입력하십시오.",
							    "fetchError" : "사용자 세부 정보를 가져 오는 중에 오류가 발생했습니다. 다시 로그인하거나 지원 팀에 문의하십시오.",
							    "updSuccess" : "게스트 정보가 성공적으로 업데이트되었습니다.",
							    "updError" : "게스트를 업데이트하는 중 오류가 발생했습니다.",
							    "dltError" : "게스트를 삭제하는 중 오류가 발생했습니다.",
							    "confirmDel" : "손님 삭제 확인",
							    "dltButton" : "지우다",
							    "cnclButton" : "취소",
							    "notExist" : "요청한 손님이 더 이상 존재하지 않습니다.",
							    "orgMaxParty1" : "10명 이상일경우 안내원에게 확인하세요 ",
							    "orgMaxParty2" : " 사람."
							  }
						}
					
					$scope.loadGuestPage = function(){
						var defered=$q.defer();
						var postBody = {};
						var url = "/kyobee/web/rest/waitlistRestAction/guestuuid/V2?uuid="+$scope.tid;
						 
						KyobeeUnsecuredService.getDataService(url , postBody)
							.query(postBody, function(data) {
								console.log(data);
								if (data.status == "SUCCESS") {
									$scope.guest = data.serviceResult;
									console.log("guest details -- "+JSON.stringify($scope.guest));
									if($scope.guest == null || $scope.guest == 'undefined'){
										return;
									}
									$scope.selectedSeatPref = [];
									if($scope.guest.seatingPreference != null && $scope.guest.seatingPreference != 'undefined'){
										var seatingPrefs = $scope.guest.seatingPreference.split(',');
										console.log("new is--"+JSON.stringify($scope.seatPrefs)+" length "+seatingPrefs.length);
										if(seatingPrefs != null && seatingPrefs != 'undefined'){
											for(var i=0; i<seatingPrefs.length; i++){
												if(seatingPrefs[i]=="")
												{
												  break;	
												}
												$scope.selectedSeatPref[i] = parseInt(seatingPrefs[i]);
												
											}
										}
										console.log($scope.selectedSeatPref);
									}
									
									
									/*change by sunny (31-07-2018)*/
									$scope.selectedGuestMarketingPref = [];
									if($scope.guest.marketingPreference != null && $scope.guest.marketingPreference != 'undefined'){
										var marketingPref = $scope.guest.marketingPreference.split(',');
										if(marketingPref != null && marketingPref != 'undefined'){
											for(var i=0; i<marketingPref.length; i++){
												$scope.selectedGuestMarketingPref.push(parseInt(marketingPref[i]));
											}
										}
										console.log($scope.selectedGuestMarketingPref);
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
									$scope.loadMarketingPref($scope.guest.organizationID);
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
						var url = '/kyobee/web/rest/waitlistRestAction/orgseatpref/V2?orgid=' + orgId;
						KyobeeUnsecuredService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.seatPrefs = data.serviceResult;
										if($scope.seatPrefs!=null)
											{
												for(var i=0;i<$scope.seatPrefs.length;i++){
													
													$scope.seatPrefs[i].prefValue=$scope.guest.languageMap[$scope.seatPrefs[i].prefKey]
												}
											}
										
										console.log("seating preference "+JSON.stringify($scope.seatPrefs));
									} else if (data.status == "FAILURE") {
										alert($scope.currentPageLanguage.fetch_error);
										$scope.logout();
									}
								}, function(error) {
									alert($scope.currentPageLanguage.fetch_error);
								});
					};
					
					/*change by sunny (31-07-2018)*/
					$scope.loadMarketingPref = function(orgId) {
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/orgMarketingPref/V2?orgid=' + orgId;
						KyobeeUnsecuredService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.marketingPref = data.serviceResult;
										if($scope.marketingPref!=null)
											{
												for(var i=0;i<$scope.marketingPref.length;i++){
													$scope.marketingPref[i].guestMarketPrefValue=$scope.guest.languageMap[$scope.marketingPref[i].guestMarketPrefKey]
												}
											}

									} else if (data.status == "FAILURE") {
										alert($scope.currentPageLanguage.fetch_error);
										$scope.logout();
									}
								}, function(error) {
									alert($scope.currentPageLanguage.fetch_error);
								});
					};
					
					
					$scope.updateGuest = function(){
						
						$scope.loading = true;
						$scope.errorMsg = null;
						
						/*if(!invalid){
							return;
						}*/
						
						if($scope.guest.name == null || $scope.guest.name == 'undefined' || $scope.guest.name == ''){
							$scope.errorMsg = $scope.currentPageLanguage.enter_name_error;
							$scope.loading = false;
							return;
						}
						
						if($scope.guest.prefType == null || $scope.guest.prefType == 'undefined'){
							$scope.errorMsg = $scope.currentPageLanguage.select_sms_error;
							$scope.loading = false;
							return;
						}
						
						if(($scope.guest.prefType == 'sms' || $scope.guest.prefType == 'SMS') && ($scope.guest.sms == null || $scope.guest.sms == 'undefined' || $scope.guest.sms == "")){
							$scope.errorMsg = $scope.currentPageLanguage.enter_num_error;
							$scope.loading = false;
							return;
						}
						
						if(($scope.guest.prefType == 'email' || $scope.guest.prefType == 'EMAIL') && ($scope.guest.email == null || $scope.guest.email == 'undefined')){
							$scope.errorMsg = $scope.currentPageLanguage.enter_email_error;
							$scope.loading = false;
							return;
						}
						
						if(($scope.guest.noOfAdults != null && $scope.guest.noOfAdults == 0) || $scope.guest.noOfAdults == undefined){
							$scope.errorMsg = $scope.currentPageLanguage.adult_size_error;
							$scope.loading=false;
							return;
						}
						
						if($scope.orgMaxParty != null && $scope.orgMaxParty != "" && ($scope.guest.noOfChildren + $scope.guest.noOfAdults + $scope.guest.noOfInfants) > $scope.orgMaxParty){
							$scope.errorMsg = $scope.currentPageLanguage.org_max_party_1+$scope.orgMaxParty+$scope.currentPageLanguage.org_max_party_2;
							$scope.loading = false; 
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
						
						// change by sunny (31-07-2018)
						
						var selectedGuestMarketingPref = [];
						
						if($scope.marketingPref!=null) {
							for(i=0;i<$scope.marketingPref.length;i++){
								for(j=0; j<$scope.selectedGuestMarketingPref.length;j++){
									if($scope.marketingPref[i].guestMarketPrefValueId == $scope.selectedGuestMarketingPref[j]){
										selectedGuestMarketingPref.push($scope.marketingPref[i]);
										break;
									}
								}								
							}
						}
						
						
						
						var postBody = {
								'name' : 	$scope.guest.name,
								'guestID' : $scope.guest.guestID,
								'organizationID' : $scope.guest.organizationID,
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
								'guestMarketingPreferences': selectedGuestMarketingPref,
								'languagePref' : $scope.guest.languagePrefID
						}
						console.log(JSON.stringify(postBody));
						$scope.successMsg=null;
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
							            $scope.successMsg="Your changes save successfully!";
							            //setTimeout(function(){ alert(); }, 1000);

										//alert($scope.currentPageLanguage.upd_success);
									} else if (data.status == "FAILURE") {
										alert($scope.currentPageLanguage.upd_error);
									}
								}, function(error) {
									alert($scope.currentPageLanguage.upd_error);
								});
					}
					
					$scope.deleteGuest = function(){
						$scope.loading = true;
						var postBody = $scope.guest;
						var url = '/kyobee/web/rest/waitlistRestAction/deleteGuest';
						KyobeeUnsecuredService.postService(url, '').query(postBody,
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
										alert($scope.currentPageLanguage.dlt_error);
									}
								}, function(error) {
									alert($scope.currentPageLanguage.dlt_error);
								});
						
					}
					
					$scope.showDeletePopup = function(){
						//$scope.scrollToTop();
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
										$scope.orgMaxParty = data.serviceResult.ORG_MAX_PARTY;
									} else if (data.status == "FAILURE") {
										alert($scope.currentPageLanguage.fetch_error);
										$scope.logout();
									}
								}, function(error) {
									alert($scope.currentPageLanguage.fetch_error);
								});
					};
					
					$scope.init = function(){
						
						if ($routeParams.tid != null && $routeParams.tid != 'undefined'){
							$scope.tid = $routeParams.tid;
							var promise = $scope.loadGuestPage();
							promise.then(function(){
								//debugger;
								$scope.currentPageLanguage=$scope.guest.languageMap;
								/*if($scope.guest.languagePrefID.langId == 1){
									$scope.currentPageLanguage = $scope.pageLanguage.en;
								}
								else if($scope.guest.languagePrefID.langId == 134){
									$scope.currentPageLanguage = $scope.pageLanguage.chi;
								}
								else if($scope.guest.languagePrefID.langId == 61){
									$scope.currentPageLanguage = $scope.pageLanguage.ko;
								}
								else if($scope.guest.languagePrefID.langId == 137){
									$scope.currentPageLanguage = $scope.pageLanguage.zhHant;
								}*/
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
										alert($scope.currentPageLanguage.fetch_error);
										$scope.logout();
									}
								}, function(error) {
									alert($scope.currentPageLanguage.fetch_error);
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
					$scope.hideSuccessMsg = function(){
						       $scope.successMsg = null;
						      };
					
					$scope.init();
					
					

				} ]);
