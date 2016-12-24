'use strict';
var KyobeeService = angular.module('Kyobee', [ 'ngResource' ]);
KyobeeService.factory('KyobeeService', [ '$resource', '$http', function($resource, $http) {
	return {		
		/* Generic Service */
		
		getDataService : function(url, requestParams) {
			return $resource(url, {}, {
				query : {
					method : 'GET',
					params : requestParams,
					headers : {
						'Content-Type' : 'application/json'
					},
					isArray : false
				}
			});
		},
		
		postDataService : function(url) {
			return $resource(url, {}, {
				query : {
					method : 'POST',
					params : {},
					headers : {
						'Content-Type' : 'application/json'
					},
					isArray : false
				}
			});
		},
		
	};
} ])
