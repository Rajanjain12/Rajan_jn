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
				'$rootScope',
				'$location',
				'$timeout',
				'$interval',
				'KyobeeService','$q',
				function($scope,$rootScope, $location, $timeout, $interval, KyobeeService,$q) {

					$rootScope.hideHeader=false;//To hide show header in index.html
					$scope.userDTO = null;
		            $scope.seatPrefs = null;
		            $scope.marketingPref = null; // change by sunny (27-07-2018)
		            $scope.authToken = 'Unno1adyiSooIAkAEt';
		            $scope.connectionUrl = 'https://ortc-developers.realtime.co/server/2.1';
		            $scope.homeCtrlLoaded = null;
		            $scope.logoImgSrc = logoImgSrc;
		            $scope.loading = false;
		            $scope.searchStatus = false;
		            $scope.pageNo=1;
		            $scope.pageNoHistory=1;
		            
		        	console.log('src' + $scope.logoImgSrc);
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
						case "partyWaiting" :
							$location.path("/partyWaiting");
							$location.search({});
							break;
						default:
							$location.path("/home");
							$location.search({});
							break;
						}
						;
					};
					
					
					
					$scope.toggleSearch = function(){
						if($scope.searchStatus){
							$scope.searchStatus = false;
							// load normal waitlist
							var waitListCtrlScope=angular.element('#search-box').scope();
							if(waitListCtrlScope.showHistory == false){
								waitListCtrlScope.loadWaitListPage(1);
							}
							else{
								waitListCtrlScope.loadHistoryPage(1);
							}
								
						}else {
							$scope.searchStatus = true;
						}						
					}
					
					$scope.moveToUpdateGuest = function(guestId){
						$location.path("/addGuest");
						$location.search({"guestId":guestId});
					}
					$scope.fetchUserDetails = function() {
						var defered=$q.defer();
						var postBody = {

						};
						var url = '/kyobee/rest/userDetails';
						$scope.homeCtrlLoaded = KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {										
										$scope.userDTO = data.serviceResult;
										put("USER_OBJ",JSON.stringify(data.serviceResult));
										
										
									} else if (data.status == "ERROR") {
										alert('Error while fetching user details. Please login again or contact support');
										$scope.logout();
									}
									defered.resolve();
								}, function(error) {
									defered.reject();
									alert('Error while fetching user details. Please login again or contact support');
								});
						return defered.promise;
						//console.log('temp'+temp);
					};
					
					/*for implementing dynamic footer (krupali 13/07/2017)*/
					
					$scope.loadPropertyInfo = function() {
						
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/propertyFileInfo';
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.footerMsg = data.serviceResult.FOOTER_MSG;
										//$scope.loadFactory();
									} else if (data.status == "FAILURE") {
										alert('Error while fetching user details. Please login again or contact support');
										$scope.logout();
									}
								}, function(error) {
									alert('Error while fetching user details. Please login again or contact support');
								});
					};
					
					/**/
					
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
					
					/*$scope.searchGrid = function(searchName) {
						alert("in searching");
						
						$scope.pagerRequest = {
								filters : null,
								sort : null,
								sortOrder: null,
								pageSize : $scope.pageSize,
								pageNo : 1
						}
						
						var postBody = {
								orgid : $scope.userDTO.organizationId,
								partyType : "C",
								searchName : $scope.searchName,
								pagerReqParam : $scope.pagerRequest								
						};
						alert("after postbody");
						$scope.loadOrgMetricks();
						alert("after loading");
						var url = '/kyobee/web/rest/waitlistRestAction/searchuser';
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log("Postbody "+JSON.stringify(postBody));
									console.log("Waitlist Grid data : "+ JSON.stringify(data));
									if (data.status == "SUCCESS") {
										alert("its success");
										var paginatedResponse = data.serviceResult;
										$scope.guestWaitList = paginatedResponse.records;
										alert("guestWaitList" + JSON.stringify(paginatedResponse));
										alert("after variable");
										$scope.pager = 	KyobeeService.getPager(paginatedResponse.totalRecords, paginatedResponse.pageNo, $scope.pageSize);
										alert(paginatedResponse.totalRecords);
										alert(pageNo);
										alert($scope.pageSize);
										alert("record displayed");
										console.log($scope.pager);
									}else if(data.status == null) {
										$scope.guestWaitList = null;
										$scope.pager = {};
									}else if (data.status == "FAILURE") {
										alert('Error while fetching wait times.');
									}
								}, function(error) {
									alert('Error while fetching wait times.. Please login again or contact support');
								});
					};*/
					
					
					$scope.loadSeatingPref = function() {
						var defered=$q.defer();
						var postBody = {

						};
						var url = '/kyobee/web/rest/waitlistRestAction/orgseatpref?orgid=' + $scope.userDTO.organizationId;;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.seatPrefs = data.serviceResult;
									} else if (data.status == "FAILURE") {
										alert('Error while fetching seating pref. Please login again or contact support');
										$scope.logout();
									}
									defered.resolve();
								}, function(error) {
									defered.reject();
									alert('Error while fetching seating pref. Please login again or contact support');
								});
						return defered.promise;
					};
					
					// change by sunny (27-07-2018)
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
												$scope.marketingPref = data.serviceResult;
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
							};


					
					$scope.resetGuestByOrgid = function() {
						$scope.loading=true;
						var postBody = {

						};
						console.log("resetGuest method - checking orgID"+$scope.userDTO.organizationId);
						var url = '/kyobee/web/rest/waitlistRestAction/reset?orgid=' + $scope.userDTO.organizationId;
						KyobeeService.getDataService(url, '').query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.$broadcast ('someEvent');
										$scope.loading = false;
										$('#resetPopup').simplePopup().hide();
										$(".simplePopupBackground").fadeOut("fast");
										$scope.successMsg = "Waitlist reset Successfully."
									} else if (data.status == "FAILURE") {
										alert('Error while resetting waitlist.');
										$scope.loading=false;
									}
								}, function(error) {
									alert('Error while resetting waitlist.. Please login again or contact support');
									$scope.loading=false;
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
					
					$scope.showResetPopup = function(){
						$('#resetPopup').simplePopup();
					}
					
					$scope.cancelReset = function(){
						$('#resetPopup').simplePopup().hide();
						$(".simplePopupBackground").fadeOut("fast");
						$scope.selectedGuest = null;
					}
					
					$scope.loadDataForPage = function(){	
						var defered =$q.defer();
						var promiseseating=$scope.loadSeatingPref();
						promiseseating.then(function(){
							var promiseMatch=$scope.loadMarketingPref();
							promiseMatch.then(function(){
							console.log("matchpref");
							defered.resolve();
							},function(error){
								defered.reject();
							});	
						},function(error){
							defered.reject();
						});	
						
						return defered.promise;
					}
					
					$scope.getArrayFromNum = function(num){
						return new Array(num);
					}
					
					$scope.convertMinstoMMHH = function(min){
						var h = Math.floor(min/60);
						h = h.toString().length == 1 ? (0+h.toString()) : h ;
						var m = min%60;
						m = m.toString().length == 1 ? (0+m.toString()) : m ;
						return h + ":" + m;
					}
					
					$scope.scrollToTop = function() {
						verticalOffset = typeof (verticalOffset) != 'undefined' ? verticalOffset : 0;
						element = $('body');
						offset = element.offset();
						offsetTop = offset.top;
						$('html, body').animate({scrollTop : offsetTop}, 500, 'linear');
					}
					
					var promise = $scope.fetchUserDetails();
					promise.then(function(){
						var promisedata=$scope.loadDataForPage();
						promisedata.then(function(){
							console.log("data fetched successfully");
						},function(error){
							
						})
					},function(error){
						
					});	
					//$scope.loadInfo();
					
				} ]);
