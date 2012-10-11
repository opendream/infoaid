angular.module('itemService', ['ngResource']).
	factory('PageItem', function ($resource) {
		var PageItem = $resource(baseUrl + '/api/page/:slug/items');
		return PageItem;
	});

angular.module('itemSidebar', ['itemService'])
	.directive('infoaidItem', function () {
		return {
			restrict: 'C',
			link: function (scope, element, attr) {

				scope.$watch(attr.name, function (value) {
					element.popover({
						trigger: 'hover',
						placement: 'top',
						title: attr.name,
						content: function (el) {

							var supply = $('.bar-supply', element).attr('data-supply').toString(),
								demand = $('.bar-demand', element).attr('data-demand').toString()
							;

							var content = 
								'<div class="supply-demand-compare">' +
									'<span class="supply-number">' + supply + '</span>' +
									' / ' +
									'<span class="supply-demand">' + demand + '</span>' +
								'</div>';

							return content;
						}
					});
				});
				
			}
		};
	})
	.directive('bar', function ($compile) {
		return {
			restrict: 'C',
			link: function (scope, element, attr) {

				if (element.hasClass('bar-supply')) {
					scope.$watch(attr.percentSupply, function (value) {
						animateProgressBar(element, attr.percentSupply);
					});
				}
				else if (element.hasClass('bar-demand')) {
					scope.$watch(attr.percentDemand, function (value) {
						animateProgressBar(element, attr.percentDemand);
					});
				}

			}
		};
	});

var ItemSidebarCtrl = function ($scope, $element, PageItem, Post, PostsBroadcast) {
	$scope.items = PageItem.query({slug: $scope.slug});

	$scope.loadItem = function(id) {
        Post('item_history').query({
            itemId: id,
            slug: $scope.slug,
            until: '',
            limit: 10
        }, function (posts) {
            PostsBroadcast.prepForBroadcast('item_history', posts);      
        });
    }

	var inArray = function (array, obj) {
		var foundIndex = -1;

		$.each(array, function (index, item) {
			if (item.name === obj.name) {
				foundIndex = index;
			}
		});

		return foundIndex;
	};

	$scope.$on('postsBroadcast', function () {
		PageItem.query({slug: $scope.slug}, function (resp) {
			angular.forEach(resp, function (item) {
				if (-1 != (foundIndex = inArray($scope.items, item))) {
					$scopeItem = $scope.items[foundIndex];

					var progress = $('.progress', $element)[foundIndex],
						supply = $('.bar-supply', progress),
						demand = $('.bar-demand', progress)
					;

					supply.attr('data-percent-supply', item.percentSupplyPerDemand);
					demand.attr('data-percent-demand', item.percentDemandPerSupply);

					animateProgressBar(supply, item.percentSupplyPerDemand);
					animateProgressBar(demand, item.percentDemandPerSupply);
				}
				else {
					$scope.items.push(item);
				}
			});

			angular.forEach($scope.items, function (item) {
				if (-1 === (foundIndex = inArray(resp, item))) {
					$scope.items.splice(foundIndex, 1);
				}
			});
		});
	});
};

var animateProgressBar = function (element, percent) {
	$(element).css('width', parseFloat(percent) + '%');
};