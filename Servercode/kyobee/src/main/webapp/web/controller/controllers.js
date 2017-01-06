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
		            $scope.seatPrefs = null;
		            $scope.authToken = 'Unno1adyiSooIAkAEt';
		            $scope.connectionUrl = 'https://ortc-developers.realtime.co/server/2.1';
					
					$scope.changeView = function(view, searchParms) {
						switch (view) {
						case "home":
							$location.path("/home");
							$location.search({});
							break;
						case "addGuest":
							$location.path("/addGuest");
							$location.search({});
							break;
						case "logout":
							$location.path("/logout");
							$location.search({});
							break;
						default:
							$location.path("/home");
							$location.search({});
							break;
						}
						;
					};
					
					$scope.moveToUpdateGuest = function(guestId){
						$location.path("/addGuest");
						$location.search({"guestId":guestId});
					}

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
					
					
					
					
					
					$scope.loadSeatingPref = function() {
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/orgseatpref?orgid=' + $scope.userDTO.organizationId;;
						KyobeeService.getDataService(url, '').query(postBody,
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
						$scope.loadSeatingPref();
					}
					
					$scope.getArrayFromNum = function(num){
						return new Array(num);
					}
					
					$scope.fetchUserDetails();
					//$scope.loadInfo();

				} ]);
