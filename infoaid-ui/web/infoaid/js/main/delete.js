angular.module('delete', [])
	.directive('buttonDelete', ['$parse', function ($parse) {
		var confirmFunc = function (scope, element, attr, event, ngClick) {
			if (! confirm("Are you sure?")) {
				event.preventDefault();
			}
			else {
				// Copy from ngEventDirectives initialization.
				var fn = $parse(ngClick);
				scope.$apply(function() {
					fn(scope, {$event:event});
				});
			}
		};

		return {
			restrict: 'C',
			compile: function (element, attr) {
				var ngClick = attr.ngClick;
				attr.ngClick = '';

				return {
					pre: function (scope, element, attr) {
						element.click(function (e) {
							confirmFunc(scope, element, attr, e, ngClick);
						});
					}
				};
			}
		};
	}]);