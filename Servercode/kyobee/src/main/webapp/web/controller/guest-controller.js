KyobeeControllers
		.controller(
				'guestCtrl',
				[
						'$scope',
						'$rootScope',
						'$location',
						'$timeout',
						'$interval',
						'$routeParams','$q',
						'KyobeeService',
						function($scope, $rootScope, $location, $timeout,
								$interval, $routeParams,$q, KyobeeService) {

							$rootScope.hideHeader = false;// To hide show
							// header in
							// index.html
							$scope.guestDTO = null;
							$scope.skipPref = null;
							$scope.guestPref = null;
							$scope.guestMarketingPref = null; // change by
							// sunny
							// (27-07-2018)
							$scope.errorMsg = null;
							$scope.successMsg=null;
							$scope.editMode = false;
							$scope.phnRegex = "^([0-9]{3}|[0-9]{3})[0-9]{3}[0-9]{4}$";
							$scope.loading = false;
							$scope.popupTitle="";
							$scope.popupdescription="";
							$scope.initAddGuest = function() {
								$scope.guestDTO = {
									name : null,
									organizationID : $scope.userDTO.organizationId,
									noOfAdults : null,
									noOfChildren : null, // changes by
									// krupali, line 24
									// to 29
									// (15/06/2017)
									noOfInfants : null,
									noOfPeople : null,
									quoteTime : null,
									partyType : null,
									prefType : 'sms',
									languagePref : {
										langIsoCode : 'en',
										langName : 'English',
										langId : 1
									},
									email : null,
									sms : null,
									optin : true,
									note : null
								}

								// we have loaded the seat prefs in main
								// controller to avoid loading again & again on
								// child controller.
								// But if user refreshes page or directly goes
								// to add page via url,
								// then parent controller seat prefs are not
								// available. Thus fetching them explicitly on
								// those scenarios.
								
								if ($scope.marketingPref == null
										|| $scope.marketingPref == undefined) {
									$scope.loadMarketingPref();
								} else {
									$scope.guestMarketingPref = angular.copy($scope.marketingPref);
								}
								console.log("GuestMarketingPRef popup : "+ JSON.stringify($scope.guestMarketingPref));
								
								if ($scope.seatPrefs == null || $scope.seatPrefs == undefined) {
									$scope.loadSeatingPref();
								} else {
									$scope.guestPref = angular.copy($scope.seatPrefs);
								}
								console.log("GuestPRef popup : "+ JSON.stringify($scope.guestPref));

								
							}
							$scope.loadMarketingPref = function() {
								var defered=$q.defer();
								var postBody = {

								};
								var url = '/kyobee/web/rest/waitlistRestAction/orgMarketingPref?orgid='
										+ $scope.userDTO.organizationId;
								;
								KyobeeService
										.getDataService(url, '')
										.query(
												postBody,
												function(data) {
													console.log(data);
													if (data.status == "SUCCESS") {
														$scope.guestMarketingPref = data.serviceResult;
														
													
													} else if (data.status == "FAILURE") {
														alert('Error while fetching user details. Please login again or contact support');
														$scope.logout();
													}
													defered.resolve();
												},
												function(error) {
													defered.reject();
													alert('Error while fetching user details. Please login again or contact support');
												});
								return defered.promise;
								console.log("GuestMarketingPRef popup : "+ JSON.stringify($scope.guestMarketingPref));
							};
						
							
								
							$scope.loadSeatingPref = function() {
								var defered=$q.defer();
								var postBody = {

								};
								var url = '/kyobee/web/rest/waitlistRestAction/orgseatpref?orgid='
										+ $scope.userDTO.organizationId;
								;
								KyobeeService
										.getDataService(url, '')
										.query(
												postBody,
												function(data) {
													console.log(data);
													if (data.status == "SUCCESS") {
														$scope.guestPref = data.serviceResult;
														
													} else if (data.status == "FAILURE") {
														alert('Error while fetching user details. Please login again or contact support');
														$scope.logout();
													}
													defered.resolve();
												},
												function(error) {
													defered.reject();
													alert('Error while fetching user details. Please login again or contact support');
												});
								return defered.promise;
								console.log("GuestPRef popup : "+ JSON.stringify($scope.guestPref));
							};

								$scope.hideErrorMsg = function() {
								$scope.errorMsg = null;
							}

								$scope.hideSuccessMsg = function() {
									$scope.successMsg = null;
									$scope.changeView('home');
								}
							$scope.addGuest = function(invalid) {
								$scope.loading = true;
								$scope.errorMsg = null;
								$scope.successMsg=null;
								
								if (invalid) {
									$scope.loading = false;
									return;
								}

								// if( $scope.userDTO.smsRoute != null &&
								// $scope.userDTO.smsRoute != '' && (
								// $scope.guestDTO.prefType == null ||
								// $scope.guestDTO.prefType == 'undefined' )){
								
								if ($scope.userDTO.smsRoute != null
										&& $scope.userDTO.smsRoute != ''
										&& ($scope.guestDTO.prefType == null || $scope.guestDTO.prefType == 'undefined')) {
									$scope.errorMsg = "Please select sms or email";
									$scope.loading = false;
									return;
								}

								if (($scope.guestDTO.noOfAdults != null && $scope.guestDTO.noOfAdults == 0)) {
									$scope.errorMsg = "Adults must be greater than 0";
									$scope.loading = false;
									return;
								}

								if ($scope.userDTO.maxParty != null
										&& $scope.userDTO.maxParty != ""
										&& ($scope.guestDTO.noOfChildren
												+ $scope.guestDTO.noOfAdults + $scope.guestDTO.noOfInfants) > $scope.userDTO.maxParty) {
									// $scope.errorMsg = "Sorry we can only
									// allow maximum "+$scope.userDTO.maxParty+"
									// people per table at a given time";
									$scope.errorMsg = "Please check in with host for parties larger than "
											+ $scope.userDTO.maxParty
											+ " people."
									$scope.loading = false;
									return;
								}

								if (($scope.userDTO.smsRoute != null && $scope.userDTO.smsRoute != '')
										&& ($scope.guestDTO.prefType == 'sms' || $scope.guestDTO.prefType == 'SMS')
										&& ($scope.guestDTO.sms == null
												|| $scope.guestDTO.sms == 'undefined' || $scope.guestDTO.sms == "")) {
									$scope.errorMsg = "Please enter the contact no.";
									$scope.loading = false;
									return;
								}

								if (($scope.guestDTO.prefType == 'email' || $scope.guestDTO.prefType == 'EMAIL')
										&& ($scope.guestDTO.email == null || $scope.guestDTO.email == 'undefined')) {
									$scope.errorMsg = "Please enter the email";
									$scope.loading = false;
									return;
								}

								if ($("#children").val() == "") {
									$("#children").val("0");
								}

								if ($("#infants").val() == "") {
									$("#infants").val("0");
								}

								if ($("#quoteTime").val() == "") {
									$("#quoteTime").val("0");
								}

								var selectedGuestPref = [];
								if ($scope.guestPref != null) {
									for (i = 0; i < $scope.guestPref.length; i++) {
										if ($scope.guestPref[i].selected) {
											selectedGuestPref
													.push($scope.guestPref[i]);
										}
									}
								}
								$scope.guestDTO.guestPreferences = selectedGuestPref;
								console.log($scope.guestDTO);

								/* chenge by sunny (27-07-2018) */
								var selectedGuestMarketingPref = [];
								if ($scope.guestMarketingPref != null) {
									for (i = 0; i < $scope.guestMarketingPref.length; i++) {
										if ($scope.guestMarketingPref[i].selected) {
											selectedGuestMarketingPref
													.push($scope.guestMarketingPref[i]);
										}
									}
								}
								$scope.guestDTO.guestMarketingPreferences = selectedGuestMarketingPref;
								console.log($scope.guestDTO);

								/* change by krupali, line 204 (15/06/2017) */
								if ($scope.guestDTO.noOfChildren == null) {
									$scope.guestDTO.noOfChildren = 0;
								}
								if ($scope.guestDTO.noOfInfants == null) {
									$scope.guestDTO.noOfInfants = 0;
								}
								$scope.guestDTO.noOfPeople = $scope.guestDTO.noOfChildren
										+ $scope.guestDTO.noOfAdults
										+ $scope.guestDTO.noOfInfants;
								var postBody = $scope.guestDTO;
								$scope.guestDTO.organizationID = $scope.userDTO.organizationId;
								var url = '/kyobee/web/rest/waitlistRestAction/addGuest';
								$("#btnSubmit").attr("disabled", true);
								KyobeeService
										.postDataService(url, '')
										.query(
												postBody,
												function(data) {
													console.log(data);
													if (data.status == "SUCCESS") {
														$scope.loading = false;
														$scope.successMsg="Guest information added successfully.";
														$timeout(function(){
														$scope.successMsg=null;
														$scope.changeView('home'); 
														}, 3000);
														/*$scope.popupTitle="Add Guest";
														$scope.popupdescription="Guest information added successfully.";

														$('#addGuestSuccessPopup').simplePopup();*/
														
													} else if (data.status == "FAILURE") {
														alert('Error while adding guest to waitlist');
													}
												},
												function(error) {
													alert('Error while adding guest to waitlist');
												});

							}
							$scope.closeSuccessPopup=function(){
								$('#addGuestSuccessPopup').simplePopup().hide();
								$(".simplePopupBackground").fadeOut("fast");
								$scope.changeView('home');

							}

							$scope.updateGuest = function(invalid) {
								$scope.loading = true;
								$scope.errorMsg = null;
								$scope.successMsg=null;
								
								if (invalid) {
									$scope.loading = false;
									return;
								}
								
								// if($scope.guestDTO.prefType == null ||
								// $scope.guestDTO.prefType == 'undefined'){
								if ($scope.userDTO.smsRoute != null
										&& $scope.userDTO.smsRoute != ''
										&& ($scope.guestDTO.prefType == null || $scope.guestDTO.prefType == 'undefined')) {
									$scope.errorMsg = "Please select sms or email";
									$scope.loading = false;
									return;
								}

								if (($scope.guestDTO.noOfAdults != null && $scope.guestDTO.noOfAdults == 0)) {
									$scope.errorMsg = "Adults must be greater than 0";
									$scope.loading = false;
									return;
								}

								if ($scope.userDTO.maxParty != null
										&& $scope.userDTO.maxParty != ""
										&& ($scope.guestDTO.noOfChildren
												+ $scope.guestDTO.noOfAdults + $scope.guestDTO.noOfInfants) > $scope.userDTO.maxParty) {
									// $scope.errorMsg = "Sorry we can only
									// allow maximum "+$scope.userDTO.maxParty+"
									// people per table at a given time";
									$scope.errorMsg = "Please check in with host for parties larger than "
											+ $scope.userDTO.maxParty
											+ " people."
									$scope.loading = false;
									return;
								}

								if (($scope.userDTO.smsRoute != null || $scope.userDTO.smsRoute != '')
										&& ($scope.guestDTO.prefType == 'sms' || $scope.guestDTO.prefType == 'SMS')
										&& ($scope.guestDTO.sms == null
												|| $scope.guestDTO.sms == 'undefined' || $scope.guestDTO.sms == "")) {
									$scope.errorMsg = "Please enter the contact no.";
									$scope.loading = false;
									return;
								}

								if (($scope.guestDTO.prefType == 'email' || $scope.guestDTO.prefType == 'EMAIL')
										&& ($scope.guestDTO.email == null || $scope.guestDTO.email == 'undefined')) {
									$scope.errorMsg = "Please enter the email";
									$scope.loading = false;
									return;
								}

								if ($("#children").val() == "") {
									$("#children").val("0");
								}

								if ($("#infants").val() == "") {
									$("#infants").val("0");
								}

								if ($("#quoteTime").val() == "") {
									$("#quoteTime").val("0");
								}

								var selectedGuestPref = [];

								if ($scope.guestPref != null) {
									for (i = 0; i < $scope.guestPref.length; i++) {
										if ($scope.guestPref[i].selected) {
											selectedGuestPref
													.push($scope.guestPref[i]);
										}
									}
								}
								$scope.guestDTO.guestPreferences = selectedGuestPref;

								/* change by sunny (27-07-2018) */
								var selectedGuestMarketingPref = [];

								if ($scope.guestMarketingPref != null) {
									for (i = 0; i < $scope.guestMarketingPref.length; i++) {
										if ($scope.guestMarketingPref[i].selected) {
											selectedGuestMarketingPref
													.push($scope.guestMarketingPref[i]);
										}
									}
								}
								$scope.guestDTO.guestMarketingPreferences = selectedGuestMarketingPref;

								/* change by krupali, line 204 (15/06/2017) */
								$scope.guestDTO.noOfPeople = $scope.guestDTO.noOfChildren
										+ $scope.guestDTO.noOfAdults
										+ $scope.guestDTO.noOfInfants;
								console.log($scope.guestDTO);

								var postBody = $scope.guestDTO;
								$("#btnUpdate").attr("disabled", true);
								var url = '/kyobee/web/rest/waitlistRestAction/updateGuestInfo';
								KyobeeService
										.postDataService(url, '')
										.query(
												postBody,
												function(data) {
													console.log(data);
													if (data.status == "SUCCESS") {
														$scope.loading = false;
														$scope.successMsg="Guest information updated successfully.";
														$timeout(function(){
														$scope.successMsg=null;
														$scope.changeView('home'); 
														}, 3000);
														/*$scope.popupTitle="Update Guest";
														$scope.popupdescription="Guest information updated successfully.";

														$('#addGuestSuccessPopup').simplePopup();*/
													} else if (data.status == "FAILURE") {
														alert('Error while updating guest');
													}
												},
												function(error) {
													alert('Error while updating guest');
													$scope.loading = false;
												});

							}

							/*
							 * $scope.fetchOrgSMSTemplates = function(OrgId) {
							 * var postBody = { }; var url =
							 * '/kyobee/web/rest/waitlistRestAction/fetchSMSTemplates?organizationID='+OrgId;
							 * KyobeeService.getDataService(url,
							 * '').query(postBody, function(data) {
							 * console.log("Updated data
							 * "+JSON.stringify(data)); if (data.status ==
							 * "SUCCESS") { $scope.guestDTO =
							 * data.serviceResult; console.log($scope.guestDTO);
							 * $scope.guestPref =
							 * angular.copy($scope.seatPrefs);
							 * if($scope.guestDTO.guestPreferences!=null)
							 * for(var i=0;i<
							 * $scope.guestDTO.guestPreferences.length;i++){
							 * for(var j=0; j < $scope.guestPref.length ; j++){
							 * if($scope.guestDTO.guestPreferences[i].prefValueId ==
							 * $scope.guestPref[j].prefValueId){
							 * $scope.guestPref[j].selected = true; break; } } } }
							 * else if (data.status == "FAILURE") { alert('Error
							 * while fetching guest details. Please login again
							 * or contact support'); $scope.logout(); } },
							 * function(error) { alert('Error while fetching
							 * guest details. Please login again or contact
							 * support'); }); }
							 */

							$scope.loadGuestToUpdate = function(guestId) {
								var postBody = {

								};
								var url = '/kyobee/web/rest/waitlistRestAction/guest?guestid='
										+ guestId;
								KyobeeService
										.getDataService(url, '')
										.query(
												postBody,
												function(data) {
													debugger;
													console
															.log("Updated data "
																	+ JSON
																			.stringify(data));
													if (data.status == "SUCCESS") {
														$scope.guestDTO = data.serviceResult;
														console.log($scope.guestDTO);
														$scope.guestPref = angular.copy($scope.seatPrefs);
														
														if ($scope.guestDTO.guestPreferences != null){
															for (var i = 0; i < $scope.guestDTO.guestPreferences.length; i++) {
																if($scope.guestPref!=null)
																	{
																		for (var j = 0; j < $scope.guestPref.length; j++) {
																			if ($scope.guestDTO.guestPreferences[i].prefValueId == $scope.guestPref[j].prefValueId) {
																				$scope.guestPref[j].selected = true;
																				break;
																			}
																		}
																	}
																
															}
														}
														/*
														 * change by sunny
														 * (27-07-2018)
														 */
														//console.log("sunny"+$scope.seatPrefs);
														/*if ($scope.marketingPref == null
																|| $scope.marketingPref == undefined) {
															$scope.loadMarketingPref();
														} else {
															$scope.guestMarketingPref = angular
																	.copy($scope.marketingPref);
														}*/
														$scope.guestMarketingPref = angular.copy($scope.marketingPref);
														if ($scope.guestDTO.guestMarketingPreferences != null){
															
															for (var i = 0; i < $scope.guestDTO.guestMarketingPreferences.length; i++) {
																console.log("Guest marketing pref "+JSON.stringify($scope.guestMarketingPref));
																if($scope.guestMarketingPref!=null)
																	{
																		for (var j = 0; j < $scope.guestMarketingPref.length; j++) {
																			if ($scope.guestDTO.guestMarketingPreferences[i].guestMarketPrefValueId == $scope.guestMarketingPref[j].guestMarketPrefValueId) {
																				$scope.guestMarketingPref[j].selected = true;
																				break;
																			}
																		}
																	}
															 }
														}
														if ($scope.guestDTO.prefType == 'sms'
																|| $scope.guestDTO.prefType == 'SMS') {
															$scope.guestDTO.sms = Number
																	.parseInt($scope.guestDTO.sms);
															$scope.guestDTO.prefType = 'sms';
															showsms();
														} else if ($scope.guestDTO.prefType == 'email'
																|| $scope.guestDTO.prefType == 'EMAIL') {
															showemail();
															$scope.guestDTO.prefType = 'email';
														}

														if ($scope.guestDTO.partyType == 0) {

														}
													} else if (data.status == "FAILURE") {
														alert('Error while fetching guest details. Please login again or contact support');
														$scope.logout();
													}
												},
												function(error) {
													alert('Error while fetching guest details. Please login again or contact support');
												});
							};

							if ($routeParams.guestId == null
									|| $routeParams.guestId == undefined) {
								
								if($scope.userDTO==null)
								{
									var promise = $scope.fetchUserDetails();
									promise.then(function(){
										var promisedata=$scope.loadDataForPage();
										promisedata.then(function(){
											$scope.initAddGuest();
										},function(error){
											
										});
										
									},function(error){
										
									});	
								 }
								else
									{
									 $scope.initAddGuest();
									}	
								$("#btnSubmit").attr("disabled", false);
								$scope.editMode = false;
							} else {
								$("#btnUpdate").attr("disabled", false);
								console.log('Load Update Guest');
								if($scope.userDTO==null)
								{
									var promise = $scope.fetchUserDetails();
									promise.then(function(){
										var promisedata=$scope.loadDataForPage();
										promisedata.then(function(){
											$scope.loadGuestToUpdate($routeParams.guestId);
										},function(error){
											
										});			
									},function(error){
										
									});	
								}
								else
								{
									
									if ($scope.seatPrefs == null || $scope.seatPrefs == undefined || $scope.marketingPref == null
											|| $scope.marketingPref == undefined) {
										var promisedata=$scope.loadDataForPage();
										promisedata.then(function(){
											$scope.loadGuestToUpdate($routeParams.guestId);
										},function(error){
											
										});			
									}
									else
										{
										$scope.loadGuestToUpdate($routeParams.guestId);
										}
									/*if ($scope.marketingPref == null
											|| $scope.marketingPref == undefined) {
										$scope.loadMarketingPref();
									} else {
										$scope.guestMarketingPref = angular.copy($scope.marketingPref);
									}
									console.log("GuestMarketingPRef popup : "+ JSON.stringify($scope.guestMarketingPref));
									
									if ($scope.seatPrefs == null || $scope.seatPrefs == undefined) {
										$scope.loadSeatingPref();
									} else {
										$scope.guestPref = angular.copy($scope.seatPrefs);
									}
									console.log("GuestPRef popup : "+ JSON.stringify($scope.guestPref));*/
									//
								}
								$scope.editMode = true;
							}

						} ]);
