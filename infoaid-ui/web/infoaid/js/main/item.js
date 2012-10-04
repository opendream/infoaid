angular.module('itemService', ['ngResource']).
	factory('PageItem', function ($resource) {
		var PageItem = $resource(baseUrl + '/api/page/:slug/items');
		return PageItem;
	});

angular.module('itemSidebar', ['itemService']);

var ItemSidebarCtrl = function ($scope, PageItem) {
	$scope.items = PageItem.query({slug: $scope.slug});
};