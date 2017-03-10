
KyobeeControllers.controller('guestCtrl',
		[
				'$scope',
				'$rootScope',
				'$location',
				'$timeout',
				'$interval',
				'$routeParams',
				'KyobeeService',
				function($scope,$rootScope, $location, $timeout, $interval, $routeParams, KyobeeService) {
					
					$rootScope.hideHeader=false;//To hide show header in index.html
					$scope.guestDTO = null;
					$scope.guestPref = null;
					$scope.errorMsg = null;
					$scope.editMode = false;	
					$scope.phnRegex="^([0-9]{3}|[0-9]{3})[0-9]{3}[0-9]{4}$";
					
					$scope.initAddGuest = function(){
						$scope.guestDTO = {
								name: null,
								organizationID : $scope.userDTO.organizationId,
								noOfPeople : null,
								prefType : null,
								email : null,
								sms : null,
								optin : false,
								note : null
						}
						$scope.errorMsg = null;
						
						// we have loaded the seat prefs in main controller to avoid loading again & again on child controller. 
						//But if user refreshes page or directly goes to add page via url, 
						//then parent controller seat prefs are not available. Thus fetching them explicitly on those scenarios.
						if($scope.seatPrefs == null || $scope.seatPrefs == undefined){
							$scope.loadSeatingPref();
						} else {
							$scope.guestPref = angular.copy($scope.seatPrefs);
						}
						console.log("GuestPRef popup : "+ JSON.stringify($scope.guestPref));
					}
					
					$scope.loadSeatingPref = function() {
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/orgseatpref?orgid=' + $scope.userDTO.organizationId;;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.guestPref = data.serviceResult;
									} else if (data.status == "FAILURE") {
										alert('Error while fetching user details. Please login again or contact support');
										$scope.logout();
									}
								}, function(error) {
									alert('Error while fetching user details. Please login again or contact support');
								});
						console.log("GuestPRef popup : "+ JSON.stringify($scope.guestPref));
					};
					
					$scope.hideErrorMsg = function(){
						$scope.errorMsg = null;
					}
					
					$scope.addGuest = function(invalid){
						
						$scope.errorMsg = null;
						
						if(invalid){
							return;
						}
						
						if($scope.guestDTO.prefType == null || $scope.guestDTO.prefType == 'undefined'){
							$scope.errorMsg = "Please select sms or email";
							return;
						}
						
						if(($scope.guestDTO.prefType == 'sms' || $scope.guestDTO.prefType == 'SMS') && ($scope.guestDTO.sms == null || $scope.guestDTO.sms == 'undefined')){
							$scope.errorMsg = "Please enter the contact no.";
							return;
						}
						
						if(($scope.guestDTO.prefType == 'email' || $scope.guestDTO.prefType == 'EMAIL') && ($scope.guestDTO.email == null || $scope.guestDTO.email == 'undefined')){
							$scope.errorMsg = "Please enter the email";
							return;
						}
						
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
					
					$scope.updateGuest = function(invalid){
						
						$scope.errorMsg = null;
						
						if(invalid){
							return;
						}
						
						if($scope.guestDTO.prefType == null || $scope.guestDTO.prefType == 'undefined'){
							$scope.errorMsg = "Please select sms or email";
							return;
						}
						
						if(($scope.guestDTO.prefType == 'sms' || $scope.guestDTO.prefType == 'SMS') && ($scope.guestDTO.sms == null || $scope.guestDTO.sms == 'undefined')){
							$scope.errorMsg = "Please enter the contact no.";
							return;
						}
						
						if(($scope.guestDTO.prefType == 'email' || $scope.guestDTO.prefType == 'EMAIL') && ($scope.guestDTO.email == null || $scope.guestDTO.email == 'undefined')){
							$scope.errorMsg = "Please enter the email";
							return;
						}
						
						var selectedGuestPref = [];
						
						for(i=0;i<$scope.guestPref.length;i++){
							if($scope.guestPref[i].selected){
								selectedGuestPref.push($scope.guestPref[i]);
							}
						}
						$scope.guestDTO.guestPreferences = selectedGuestPref;
						console.log($scope.guestDTO);
						
						var postBody = $scope.guestDTO;
						
						var url = '/kyobee/web/rest/waitlistRestAction/updateGuestInfo';
						KyobeeService.postDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.changeView('home');
									} else if (data.status == "FAILURE") {
										alert('Error while updating guest');
									}
								}, function(error) {
									alert('Error while updating guest');
								});
						
					}
					
					$scope.loadGuestToUpdate = function(guestId) {
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/guest?guestid='+guestId;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log("Updated data "+JSON.stringify(data));
									if (data.status == "SUCCESS") {
										$scope.guestDTO = data.serviceResult;
										$scope.guestPref = angular.copy($scope.seatPrefs);
										if($scope.guestDTO.guestPreferences!=null)
										for(var i=0;i< $scope.guestDTO.guestPreferences.length;i++){
											for(var j=0; j < $scope.guestPref.length ; j++){
												if($scope.guestDTO.guestPreferences[i].prefValueId == $scope.guestPref[j].prefValueId){
													$scope.guestPref[j].selected = true;
													break;
												}
											}
										}
										if($scope.guestDTO.prefType == 'sms' || $scope.guestDTO.prefType == 'SMS'){
											$scope.guestDTO.sms = Number.parseInt($scope.guestDTO.sms);
											$scope.guestDTO.prefType = 'sms';
											showsms();
										} else if($scope.guestDTO.prefType == 'email' || $scope.guestDTO.prefType == 'EMAIL') {
											showemail();
											$scope.guestDTO.prefType = 'email';
										}
									} else if (data.status == "FAILURE") {
										alert('Error while fetching guest details. Please login again or contact support');
										$scope.logout();
									}
								}, function(error) {
									alert('Error while fetching guest details. Please login again or contact support');
								});
					};
					
					
					if($routeParams.guestId == null || $routeParams.guestId == 'undefined'){
						console.log('Load Add Guest');
						$scope.initAddGuest();
						
						$scope.editMode = false;
					} else {
						console.log('Load Update Guest');
						$scope.loadGuestToUpdate($routeParams.guestId);
						$scope.editMode = true;
					}
										

				} ]);
