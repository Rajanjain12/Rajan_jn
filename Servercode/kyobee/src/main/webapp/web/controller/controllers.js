var KyobeeControllers = angular.module('KyobeeControllers', []).factory(
		'localStorage', function() {
			return {};
		});

/**
 * The parent controller
 */
KyobeeControllers.controller('homeCtrl',
		[
				'$scope',
				'$location',
				'$timeout',
				'$interval',
				'KyobeeService',
				function($scope, $location, $timeout, $interval, KyobeeService) {

					$scope.userDTO = null;
					$scope.waitTime = null;
					$scope.notifyFirst = null;
					$scope.totalWaitTime = null;
					$scope.appKey = null;
		            $scope.privateKey = null;
		            $scope.channel = null;
		            $scope.guestWaitList = null;
					
					$scope.changeView = function(view, searchParms) {
						switch (view) {
						case "home":
							$location.path("/home");
							break;
						case "addGuest":
							$location.path("/addGuest");
							break;
						case "logout":
							$location.path("/logout");
							break;
						default:
							$location.path("/home");
							break;
						}
						;
					};

					$scope.fetchUserDetails = function() {
						var postBody = {

						};
						var url = '/kyobee/rest/userDetails';
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.userDTO = data.serviceResult;
										$scope.loadDataForPage();
									} else if (data.status == "FAILURE") {
										alert('Error while fetching user details. Please login again or contact support');
										$scope.logout();
									}
								}, function(error) {
									alert('Error while fetching user details. Please login again or contact support');
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
									} else if (data.status == "FAILURE") {
										alert('Error while fetching user details. Please login again or contact support');
										$scope.logout();
									}
								}, function(error) {
									alert('Error while fetching user details. Please login again or contact support');
								});
					};
					
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
						var url = '/kyobee/web/rest/waitlistRestAction/checkinusers?orgid=' + $scope.userDTO.organizationId + '&recordsPerPage=10&pageNumber=1';
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
					
					$scope.logout = function(){
						var postBody = {

						};
						var url = '/kyobee/web/rest/logout';
						KyobeeService.getDataService(url, '').query(postBody,	
								function(data) {
									$scope.changeView("logout");
								}, function(error) {
									alert('Session Timed Out');
									$scope.changeView("logout");
								});
					}
					
					$scope.loadDataForPage = function(){
						$scope.loadInfo();
						$scope.loadWaitListGuests();	
					}
					
					$scope.fetchUserDetails();
					
					$scope.showPopup = function(){
						$('#showpopup').simplePopup();
					}
					

				} ]);
