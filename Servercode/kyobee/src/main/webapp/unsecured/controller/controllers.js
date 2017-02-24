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
					$scope.loading=false;
					$scope.logoImgSrc = logoImgSrc;
					$scope.signUpForm =  {
							companyName : null,
							companyPrimaryPhone : null,
							companyEmail : null,
							promotionalCode : null,
							firstName : null,
							lastName : null,
							email : null,
							password : null,
							confirmPassword : null,
							mainCaptcha : null,
							inputCaptcha : null,
							clientBase : subdomain
						};
					
					
					
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
						case "signup":
							$location.path("/signup");
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
							     password: $scope.password,
							     clientBase : subdomain
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
					
					/**
					 * SIGNUP FUNCTION
					 */
					$scope.signup = function() {
						console.log("Signup called");
						/*if(invalid){
							return;
						}*/
						
						$scope.successMsg = null;
						$scope.errorMsg = null;
						$scope.loading=true;
						
						
						if($scope.signUpForm.inputCaptcha == null ){
							$scope.errorMsg = "Kindly enter the captcha.";
							$scope.scrollToTop();
							$scope.loading=false;
							return;
						}
						
						var string1 = removeSpaces($scope.mainCaptcha);
						var string2 = removeSpaces($scope.signUpForm.inputCaptcha);
						if (string1 != string2) {
							$scope.errorMsg = "The captcha does not match. Please try again.";
							$scope.signUpForm.inputCaptcha = '';
							$scope.initCaptcha();
							$scope.scrollToTop();
							$scope.loading=false;
							return;
						}
						
						if($scope.signUpForm.companyName == null){
							$scope.errorMsg = "Company Name is required";
							$scope.scrollToTop();
							$scope.loading=false;
							return;
						}
						
						if($scope.signUpForm.companyPrimaryPhone == null){
							$scope.errorMsg = "Company Primary Phone is required";
							$scope.scrollToTop();
							$scope.loading=false;
							return;
						}
						
						if($scope.signUpForm.companyEmail == null){
							$scope.errorMsg = "Company Email Address is required";
							$scope.scrollToTop();
							$scope.loading=false;
							return;
						}
						
						if($scope.signUpForm.firstName == null){
							$scope.errorMsg = "First Name is required";
							$scope.scrollToTop();
							$scope.loading=false;
							return;
						}
						
						if($scope.signUpForm.lastName == null){
							$scope.errorMsg = "Last Name is required";
							$scope.scrollToTop();
							$scope.loading=false;
							return;
						}
						
						if($scope.signUpForm.email == null){
							$scope.errorMsg = "Email Address is required";
							$scope.scrollToTop();
							$scope.loading=false;
							return;
						} else {
							$scope.signUpForm.username = $scope.signUpForm.email
						}
						
						if($scope.signUpForm.password == null){
							$scope.errorMsg = "Password is required";
							$scope.scrollToTop();
							$scope.loading=false;
							return;
						}
						
						if($scope.signUpForm.confirmPassword == null){
							$scope.errorMsg = "Confirm Password is required";
							$scope.scrollToTop();
							$scope.loading=false;
							return;
						}
						
						if($scope.signUpForm.confirmPassword != $scope.signUpForm.password){
							$scope.errorMsg = "Password does not match with the Confirm Password";
							$scope.scrollToTop();
							$scope.loading=false;
							return;
						}
						
						$scope.hideSuccessMsg = function(){
							$scope.successMsg = null;
						};
						
						$scope.hideErrorMsg = function(){
							$scope.errorMsg = null;
						};
						
						var postBody = $scope.signUpForm;
						var url = "/kyobee/rest/signup"
						 
						KyobeeUnsecuredService.postService(url , postBody)
							.query(postBody, function(data) {
								console.log(data);
								//jQuery(".loader").fadeOut('slow');
								if (data.status == "SUCCESS") {
									$scope.success = true;
									//$('#signupSuccessModal').modal('show');
									$scope.successMsg = 'You have successfully signed into Kyobee! You would have received an email.';
									$scope.loading=false;
									$('#thankYouPopup').simplePopup();
									$scope.scrollToTop();
									//$scope.changeView('login');
								} else if (data.status == "FAILURE") {
									$scope.success = false;
									$scope.errorMsg = data.errorDescription;
									$scope.scrollToTop();
									$scope.loading=false;
								}
							}, function(error) {
								$scope.success = false;
								$scope.errorMsg = "Error occured while activation. Please contact support or try again later";
								$scope.loading=false;
							});
					};
					
					$scope.scrollToTop = function() {
						verticalOffset = typeof(verticalOffset) != 'undefined' ? verticalOffset : 0;
						element = $('body');
						offset = element.offset();
						offsetTop = offset.top;
						$('html, body').animate({scrollTop: offsetTop}, 500, 'linear');
					}
					
					$scope.convertMinstoMMHH = function(min){
						var h = Math.floor(min/60);
						h = h.toString().length == 1 ? (0+h.toString()) : h ;
						var m = min%60;
						m = m.toString().length == 1 ? (0+m.toString()) : m ;
						return h + ":" + m;
					}
					
					$scope.initCaptcha = function() {
						var alpha = new Array('A', 'B', 'C', 'D', 'E',
								'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
								'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
								'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
								'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
								'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
								't', 'u', 'v', 'w', 'x', 'y', 'z', '1',
								'2', '3', '4', '5', '6', '7', '8', '9',	'0');
						var i;
						var code = "";
						for (i = 0; i < 7; i++) {
							code = code + alpha[Math.floor(Math.random() * alpha.length)] + " ";
						}
						$scope.mainCaptcha = code;
					}

					$scope.validateCaptcha = function () {
						var string1 = removeSpaces($scope.mainCaptcha);
						var string2 = removeSpaces($scope.signupForm.inputCaptcha);
						if (string1 == string2) {
							alert(true);
						}
						else {
							alert(false);
						}
					}

					removeSpaces = function (string) {
						return string.split(' ').join('');
					}
					
					$scope.hideSuccessMsg = function(){
						$scope.successMsg = null;
					};
					
					$scope.hideErrorMsg = function(){
						$scope.errorMsg = null;
					};					
					
					$scope.goToSignin = function() {
						$('#thankYouPopup').simplePopup().hide();
						$(".simplePopupBackground").fadeOut("fast");
						$scope.changeView('login');
					};
					
				} ]);
