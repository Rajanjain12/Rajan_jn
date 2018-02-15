KyobeeUnSecuredController.controller('forgotPwdCtrl', [
		'$scope',
		'$location',
		'$timeout',
		'$interval',
		'$routeParams',
		'KyobeeUnsecuredService',
		function($scope, $location, $timeout, $interval, $routeParams,
				KyobeeUnsecuredService) {
			
			$scope.email = null;
			$scope.successMsg = null;
			
			$scope.forgotPwd = function() {
				var postBody =
				{

				};
				var url = '/kyobee/rest/forgotPwd?email=' + $scope.email;
				KyobeeUnsecuredService.getDataService(url, '').query(postBody,
						function(data) {
							console.log(data);
							if (data.status == "SUCCESS") {
								$scope.successMsg = data.serviceResult;
							} else if (data.status == "FAILURE") {
								alert(data.serviceResult);
							}
						}, function(error) {
							alert("Error while sending forgot password email");
						});
			};
			
			$scope.hideSuccessMsg = function() {
				$scope.successMsg = null;
			}
		} ]);
