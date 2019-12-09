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
		
		// Below it the pager service, use to render the pagination on UI
		getPager : function (totalItems, currentPage, pageSize) {
			
	        // default to first page
	        currentPage = currentPage || 1;
	 
	        // default page size is 10
	        pageSize =  10;
	        
	        // calculate total pages
	        var totalPages = Math.ceil(totalItems / pageSize);
	 
	        var startPage, endPage;
	        if (totalPages <= 5) {
	            // less than 10 total pages so show all
	            startPage = 1;
	            endPage = totalPages;
	        } else {
	            // more than 10 total pages so calculate start and end pages
	            if (currentPage <= 2) {
	                startPage = 1;
	                endPage = 5;
	            } else if (currentPage + 2 >= totalPages) {
	                startPage = totalPages - 9;
	                endPage = totalPages;
	            } else {
	                startPage = currentPage - 2;
	                endPage = currentPage + 2;
	            }
	        }
	        
	        var startIndex = (currentPage - 1) * pageSize;
			var endIndex = Math.min(startIndex + pageSize - 1, totalItems - 1);
	        // create an array of pages to ng-repeat in the pager control
	        var pages =[];
	        for (var i = startPage; i <= endPage; i++) {
	        	pages.push(i);
	        }
	 
	        // return object with all pager properties required by the view
	        console.log("totalItems "+totalItems+" currentPage "+currentPage+" pageSize "+pageSize+" totalPages "+totalPages+" startPage "+startPage+" endPage "+endPage+" pages "+pages);
	        return {
	            totalItems: totalItems,
	            currentPage: currentPage,
	            pageSize: pageSize,
	            totalPages: totalPages,
	            startPage: startPage,
	            endPage: endPage,
	            pages: pages,
	            startIndex:startIndex,
	            endIndex:endIndex
	        };
	    },
		
	};
} ])
