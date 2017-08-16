
var KyobeeApp = angular.module('KyobeeApp', [ 'ngRoute', 'KyobeeControllers', 'Kyobee',
		'ngAnimate','ngMessages','ui-rangeSlider' ]).factory('localStorage', function () {return {};});
KyobeeApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/home', {
		templateUrl : 'waitlist.html',
		controller : 'waitListCtrl'
	}).when('/addGuest', {
		templateUrl : 'addnewguest.html',
		controller : 'guestCtrl'
	}).when('/logout', {
		templateUrl : '../index.html'
	}).when('/partyWaiting',{
		templateUrl : 'partieswaiting.html',
		controller : 'partyWaitingCtrl'
	}).	otherwise({
		redirectTo : '/home'
	});
} ]);

KyobeeApp.directive('popover', function($compile, $timeout){
  return {
    restrict: 'A',
    link:function(scope, el, attrs){
      var content = attrs.content; //get the template from the attribute
      var elm = angular.element('<div />'); //create a temporary element
      elm.append(attrs.content); //append the content
      $compile(elm)(scope); //compile 
      $timeout(function() { //Once That is rendered
        el.removeAttr('popover').attr('data-content',elm.html()); //Update the attribute
        el.popover(); //set up popover
       });
    }
  }
})
