
KyobeeControllers.controller('guestCtrl',
		[
				'$scope',
				'$location',
				'$timeout',
				'$interval',
				'KyobeeService',
				function($scope, $location, $timeout, $interval, KyobeeService) {

					$scope.guestDTO = null;
					$scope.guestPref = null;
					
					$scope.init = function(){
						$scope.guestDTO = {
								name: null,
								organizationID : $scope.userDTO.organizationId,
								noOfPeople : null,
								prefType : null,
								email : null,
								sms : null,
								optin : false
						}
						$scope.guestPref = angular.copy($scope.seatPrefs);
					}
					
					$scope.addGuest = function(){
						
						
						var selectedGuestPref = [];
						
						for(i=0;i<$scope.guestPref.length;i++){
							if($scope.guestPref[i].selected){
								selectedGuestPref.push($scope.guestPref[i]);
							}
						}
						$scope.guestDTO.guestPreferences = selectedGuestPref;
						console.log($scope.guestDTO);
						
						var postBody = $scope.guestDTO;
						
						var url = '/kyobee/web/rest/waitlistRestAction/addGuest';
						KyobeeService.postDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.changeView('home');
									} else if (data.status == "FAILURE") {
										alert('Error while adding guest to waitlist');
									}
								}, function(error) {
									alert('Error while adding guest to waitlist');
								});
						
					}
					
					
					$scope.init();
										

				} ]);
