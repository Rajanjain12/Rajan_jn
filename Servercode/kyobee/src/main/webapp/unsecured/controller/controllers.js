var KyobeeUnSecuredController = angular.module('KyobeeUnSecuredController',[]).factory(
		'localStorage', function() {
			return {};
		});

/**
 * The parent controller
 */
KyobeeUnSecuredController.controller('homeCtrl',
		[
				'$scope',
				'$location',
				'$timeout',
				'$interval', 'KyobeeUnsecuredService',
				function($scope, $location, $timeout, $interval, KyobeeUnsecuredService) {
					
					$scope.username=null;
					$scope.password=null;
					$scope.errorMsg = null;
					$scope.logoImgSrc = logoImgSrc;
					
					console.log('src' + $scope.logoImgSrc);
					
					$scope.changeView = function(view) {
						switch (view) {
						case "login":
							$location.path("/login");
							$location.search({});
							break;
						case "dashboard":
							$location.path("/dashboard");
							$location.search({});
							break;
						default:
							$location.path("/login");
							$location.search({});
							break;
						}
						;
					};
					
					$scope.login = function(invalid) {
						console.log(invalid);
						if(invalid){
							return;
						}
						
						$scope.errorMsg = null;
						
						var postBody = {
								 username: $scope.username,
							     password: $scope.password
						};
						
						var url = '/kyobee/rest/login';
						
						KyobeeUnsecuredService.postService(url).query(postBody,
								function(data) {
									console.log(data);
									if (data.status == "SUCCESS") {
										$scope.changeView('dashboard');
									} else if (data.status == "FAILURE") {
										$scope.errorMsg = data.errorDescription;
									}
								}, function(error) {
									alert('Session Timed Out');
								});
						
						console.log($scope.username);
						console.log($scope.password);
						console.log("Login called");							
					};
					
				} ]);
