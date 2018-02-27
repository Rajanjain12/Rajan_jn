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
			$scope.errorMsg = null;
			
			$scope.forgotPwd = function() {
				$scope.loading = true;
				var postBody = {

				};
				var url = '/kyobee/rest/forgotPwd?email=' + $scope.email;
				KyobeeUnsecuredService.getDataService(url, '').query(postBody,
						function(data) {
							console.log(data);
							if (data.status == "SUCCESS") {
								$scope.successMsg = data.serviceResult;
								$scope.loading = false;
							} else if (data.status == "FAILURE") {
								$scope.errorMsg = data.serviceResult;
								$scope.loading = false;
							}
						}, function(error) {
							alert("Error while sending forgot password email");
							$scope.loading = false;
						});
			};
			
			$scope.hideSuccessMsg = function() {
				$scope.successMsg = null;
			}
		} ]);
