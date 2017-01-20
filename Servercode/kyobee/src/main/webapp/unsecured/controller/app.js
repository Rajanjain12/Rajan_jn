
var KyobeeAppUnsecured = angular.module('KyobeeAppUnsecured', [ 'ngRoute', 'KyobeeUnSecuredController', 'KyobeeUnsecured', 
		'ngAnimate','ngMessages' ]).factory('localStorage', function () {return {};});
KyobeeAppUnsecured.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/login', {
		templateUrl : 'login.html',
		controller : 'homeCtrl'
	}).when('/dashboard', {
		templateUrl : 'app.html'
	}).when('/signup', {
		templateUrl : 'signup.html',
		controller : 'homeCtrl'
	}).when('/s', {
		templateUrl : 'guestcheckin.html',
	}).otherwise({
		redirectTo : '/login'
	});
} ]);
