
KyobeeControllers.controller('waitListCtrl',
		[
				'$scope',
				'$location',
				'$timeout',
				'$interval',
				'KyobeeService',
				function($scope, $location, $timeout, $interval, KyobeeService) {

					$scope.waitTime = null;
					$scope.notifyFirst = null;
					$scope.totalWaitTime = null;
					$scope.guestWaitList = null;
					$scope.selectedGuest = null;
					$scope.client = null;
					$scope.countMsgChannel = 0;
		            
		            
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
					
					$scope.loadWaitListGuests = function() {
						var postBody = {

						};
						$scope.loadOrgMetricks();
						var url = '/kyobee/web/rest/waitlistRestAction/checkinusers?orgid=' + $scope.userDTO.organizationId + '&recordsPerPage=50&pageNumber=1';
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.guestWaitList = data.serviceResult;
									} else if (data.status == "FAILURE") {
										alert('Error while fetching wait times.');
									}
								}, function(error) {
									alert('Error while fetching wait times.. Please login again or contact support');
								});
					};
					
					$scope.showPopup = function(guestObj){
						$('#showpopup').simplePopup();
						$scope.selectedGuest = guestObj;
					}
					
					$scope.showDeletePopup = function(guestObj){
						$('#deletePopup').simplePopup();
						$scope.selectedGuest = guestObj;
					}
					
					$scope.incrementCalloutCount = function(){
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
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/markAsSeated?orgId=' + $scope.userDTO.organizationId + '&guestId='+$scope.selectedGuest.guestID;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$('#showpopup').simplePopup().hide();
										$(".simplePopupBackground").fadeOut("fast");
										$scope.loadWaitListGuests();
										console.log(data);
									} else if (data.status == "FAILURE") {
										alert('Error while marking as seated.');
									}
								}, function(error) {
									alert('Error while marking as seated');
								});
						
					}
					
					$scope.deleteGuest = function(){
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/deleteGuest?orgId=' + $scope.userDTO.organizationId + '&guestId='+$scope.selectedGuest.guestID;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$('#deletePopup').simplePopup().hide();
										$(".simplePopupBackground").fadeOut("fast");
										$scope.loadWaitListGuests();
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
			                    	{ $scope.loadWaitListGuests();}
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
						
					
					
					//$scope.loadOrtcFactory();
					$scope.loadWaitListGuests();
										

				} ]);