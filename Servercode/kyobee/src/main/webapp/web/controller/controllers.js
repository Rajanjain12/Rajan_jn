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

					$scope.changeView = function(view, searchParms) {
						switch (view) {
						case "home":
							$location.path("/home");
							break;
						case "logout":
							$scope.Module = null;
							$location.path("/logout");
							break;
						default:
							$location.path("/home");
							break;
						}
						;
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
					

				} ]);
