
var KyobeeApp = angular.module('KyobeeApp', [ 'ngRoute', 'KyobeeControllers', 'Kyobee',
		'ngAnimate','ngMessages' ]).factory('localStorage', function () {return {};});
KyobeeApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/home', {
		templateUrl : 'waitlist.html',
		controller : 'homeCtrl'
	}).	otherwise({
		redirectTo : '/home'
	});
} ]);
