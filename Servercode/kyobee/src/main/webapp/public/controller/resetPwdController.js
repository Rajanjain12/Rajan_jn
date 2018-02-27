KyobeeUnSecuredController.controller('resetPwdCtrl', [
		'$scope',
		'$location',
		'$timeout',
		'$interval',
		'$routeParams',
		'KyobeeUnsecuredService',
		function($scope, $location, $timeout, $interval, $routeParams,
				KyobeeUnsecuredService) {
			
			$scope.userId = null,
			$scope.authcode = null;
			$scope.validurl = false;
			$scope.message = null;
			$scope.successMsg = null;
			$scope.errorMsg = null;
			$scope.loading = false;
			//$scope.form = {};
			$scope.newPwd = null,
			$scope.newConfPwd = null
			
			$scope.validateResetPwdURL = function() {
				$scope.loading = true;
				var postBody = {

				};
				var url = '/kyobee/rest/validateResetPwdurl?userId=' + $scope.userId+'&authcode='+ $scope.authcode;
				KyobeeUnsecuredService.getDataService(url, '').query(postBody,
						function(data) {
							console.log(data);
							if (data.status == "SUCCESS") {
								$scope.validurl = true;
							} else if (data.status == "FAILURE") {
								$scope.validurl = false;
								$scope.message = data.serviceResult;
							}
							$scope.loading = false;
						}, function(error) {
							$scope.validurl = false;
							$scope.loading = false;
							$scope.message = "Error occured while validating url. Please contact support or try again later";
						});
			};
			
			$scope.validateURL = function(){
				if($routeParams.userId != null && $routeParams.userId != 'undefined'){
					$scope.userId = $routeParams.userId;
				} 
				
				if ($routeParams.authcode != null && $routeParams.authcode != 'undefined'){
					$scope.authcode = $routeParams.authcode;
				}
				
				if($scope.userId == null || $scope.authcode == null){
					$scope.validurl = false;
					//$scope.message = "Invalid URL. Please contact support";
				} else {
					$scope.validateResetPwdURL();
				}
			}
			
			$scope.validateURL();
			$scope.reset = function(){
				$scope.resetPwdForm.$setPristine();
			}
			
			$scope.resetPassword = function(invalid){
				$scope.successMsg = null;
				$scope.errorMsg = null;
				//$scope.form.resetPwdForm.$setPristine();
				if(invalid){
					return;
				}
				
				if($scope.newPwd !== $scope.newConfPwd){
					$scope.errorMsg = "Password & Confirm Passord should match";
					return;
				}
				
				$scope.loading = true;
				
				var postBody = {
						"userId" : $scope.userId,
						"password":$scope.newPwd
				};
				var params = {};
				
				var url = '/kyobee/rest/resetPassword';
				//$scope.showLoading();
				KyobeeUnsecuredService.postService(url)
						.query(postBody, function(data) {
							console.log(data);
							if (data.status == "SUCCESS") {
								$scope.successMsg = data.serviceResult;
							} else if (data.status == "FAILURE") {
								$scope.errorMsg = data.serviceResult;
							}
							$scope.loading = false;
						}, function(error) {
							$scope.loading = false;
							$scope.errorMsg = "Error occured while password reset. Please contact support or try again later";
						});
			}
			
			$scope.hideSuccessMsg = function() {
				$scope.successMsg = null;
			}
		} ]);
