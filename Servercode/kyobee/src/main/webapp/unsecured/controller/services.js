'use strict';
var KyobeeUnsecuredService = angular.module('KyobeeUnsecured', [ 'ngResource' ]);
KyobeeUnsecuredService.factory('KyobeeUnsecuredService', [ '$resource', '$http', function($resource, $http) {
	return {
		postService : function(url) {
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
} ]);
