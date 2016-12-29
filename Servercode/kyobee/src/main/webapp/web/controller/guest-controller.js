
KyobeeControllers.controller('guestCtrl',
		[
				'$scope',
				'$location',
				'$timeout',
				'$interval',
				'KyobeeService',
				function($scope, $location, $timeout, $interval, KyobeeService) {

					$scope.guestDTO = null;
					
					
					$scope.init = function(){
						$scope.guestDTO = {
								name: null,
								OrganizationID : $scope.userDTO.organizationId,
								noOfPeople : null,
								prefType : null,
								email : null,
								sms : null,
								optin : false
						}
					}
					
					$scope.addGuest = function(){
						console.log($scope.guestDTO);
					}
					
					
					$scope.init();
										

				} ]);
