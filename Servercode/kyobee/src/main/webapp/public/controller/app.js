
var KyobeeAppUnsecured = angular.module('KyobeeAppUnsecured', [ 'ngRoute', 'KyobeeUnSecuredController', 'KyobeeUnsecured', 
		'ngAnimate','ngMessages','ui.mask' ]).factory('localStorage', function () {return {};});
KyobeeAppUnsecured.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/login', {
		templateUrl : 'login.html',
		controller : 'homeCtrl'
	}).when('/dashboard', {
		templateUrl : 'app.html'
	}).when('/signup', {
		templateUrl : 'signup.html',
		controller : 'homeCtrl'
	}).when('/forgotpwd', {
		templateUrl : 'forgotpassword.html',
		controller : 'forgotPwdCtrl'
	}).when('/resetpwd/r/:userId/:authcode', {
		templateUrl : function(params) {
			return 'resetpassword.html?userId='+params.userId+'&authcode='+params.authcode;
		},
		controller : 'resetPwdCtrl'
	}).when('/s/:tid', {
		templateUrl : function(params) {
			return 'guestcheckin.html?tid='+params.tid;
		},
		//templateUrl : 'guestcheckin.html',
		controller : 'guestDetailCtrl'
	}).otherwise({
		redirectTo : '/login'
	});
} ]);
