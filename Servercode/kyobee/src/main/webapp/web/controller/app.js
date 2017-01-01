
var KyobeeApp = angular.module('KyobeeApp', [ 'ngRoute', 'KyobeeControllers', 'Kyobee',
		'ngAnimate','ngMessages' ]).factory('localStorage', function () {return {};});
KyobeeApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/home', {
		templateUrl : 'waitlist.html',
		controller : 'waitListCtrl'
	}).when('/addGuest', {
		templateUrl : 'addnewguest.html',
		controller : 'guestCtrl'
	}).when('/logout', {
		templateUrl : '../index.html'
	}).	otherwise({
		redirectTo : '/home'
	});
} ]);
